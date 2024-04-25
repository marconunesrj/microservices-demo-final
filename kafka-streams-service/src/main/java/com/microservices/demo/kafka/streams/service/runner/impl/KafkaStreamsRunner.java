package com.microservices.demo.kafka.streams.service.runner.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaStreamsConfigData;
import com.microservices.demo.kafka.avro.model.TwitterAnalyticsAvroModel;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Component
public class KafkaStreamsRunner implements StreamsRunner<String, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsRunner.class);

    // Usar para corresponder a quaisquer caracteres que não sejam palavras de um texto
    // que usaremos para dividir o texto de entrada para obter a contagem de palavras
    private static final String REGEX = "\\W+";

    private final KafkaStreamsConfigData kafkaStreamsConfigData;

    private final KafkaConfigData kafkaConfigData;

    private final Properties streamsConfiguration;

    private KafkaStreams kafkaStreams;

    // Variável de armazenamento de chave/valor volátil.
    private volatile ReadOnlyKeyValueStore<String, Long> keyValueStore;

    // @Qualifier("streamConfiguration") -> vem da classe KafkaStreamsConfig.java
    public KafkaStreamsRunner(KafkaStreamsConfigData kafkaStreamsConfig,
                              KafkaConfigData kafkaConfig,
                              @Qualifier("streamConfiguration") Properties streamsConfiguration) {
        this.kafkaStreamsConfigData = kafkaStreamsConfig;
        this.kafkaConfigData = kafkaConfig;
        this.streamsConfiguration = streamsConfiguration;
    }

    @Override
    public void start() {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                kafkaConfigData.getSchemaRegistryUrlKey(),
                kafkaConfigData.getSchemaRegistryUrl());

        // StreamsBuilder é a classe principal para criar o fluxo Kafka
        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        final KStream<Long, TwitterAvroModel> twitterAvroModelKStream =
                getTwitterAvroModelKStream(serdeConfig, streamsBuilder);

        createTopology(twitterAvroModelKStream, serdeConfig);

        startStreaming(streamsBuilder);
    }

    @Override
    public Long getValueByKey(String word) {
        if (kafkaStreams != null && kafkaStreams.state() == KafkaStreams.State.RUNNING) {
            if (keyValueStore == null) {
                synchronized (this) {
                    if (keyValueStore == null) {
                        keyValueStore = kafkaStreams.store(StoreQueryParameters
                                .fromNameAndType(kafkaStreamsConfigData.getWordCountStoreName(),
                                        QueryableStoreTypes.keyValueStore()));
                    }
                }
            }
            return keyValueStore.get(word.toLowerCase());
        }
        return 0L;
    }

    @PreDestroy
    public void close() {
        if (kafkaStreams != null) {
            kafkaStreams.close();
            LOG.info("Kafka streaming closed!");
        }
    }


    private void startStreaming(StreamsBuilder streamsBuilder) {
        final Topology topology = streamsBuilder.build();
        LOG.info("Defined topology: {}", topology.describe());
        kafkaStreams = new KafkaStreams(topology, streamsConfiguration);
        kafkaStreams.start();
        LOG.info("Kafka streaming started..");
    }

    // Criar uma topologia na terminologia de fluxos kafka.
    private void createTopology(KStream<Long, TwitterAvroModel> twitterAvroModelKStream,
                                Map<String, String> serdeConfig) {
        Pattern pattern = Pattern.compile(REGEX, Pattern.UNICODE_CHARACTER_CLASS);

        Serde<TwitterAnalyticsAvroModel> serdeTwitterAnalyticsAvroModel = getSerdeAnalyticsModel(serdeConfig);

        // Fará o trabalho de ler os dados do tópico de entrada e inserir no tópico de saída
        twitterAvroModelKStream
                .flatMapValues(value -> Arrays.asList(pattern.split(value.getText().toLowerCase()))) // obtemos uma lista de palavras no campo de texto do objeto TwitterAvroModel
                .groupBy((key, word) -> word)  // retornará um stream de grupo, podendo ser agregado com com o método count
                .count(Materialized
                        .<String, Long, KeyValueStore<Bytes, byte[]>>as(kafkaStreamsConfigData.getWordCountStoreName()))// armazenando o estado chamado. Materialized é do tipo Bytes e byte[]
                .toStream()
                .map(mapToAnalyticsModel())  // para mapear este stream para o novo TwitterAnalyticsAvroModel
                .to(kafkaStreamsConfigData.getOutputTopicName(),
                        Produced.with(Serdes.String(), serdeTwitterAnalyticsAvroModel));

    }

    private KeyValueMapper<String, Long, KeyValue<? extends String, ? extends TwitterAnalyticsAvroModel>>
    mapToAnalyticsModel() {
        return (word, count) -> {
            LOG.info("Sending to topic {}, word {} - count {}",
                    kafkaStreamsConfigData.getOutputTopicName(), word, count);
            return new KeyValue<>(word, TwitterAnalyticsAvroModel
                    .newBuilder()
                    .setWord(word)
                    .setWordCount(count)
                    .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());
        };
    }

    private Serde<TwitterAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
        Serde<TwitterAnalyticsAvroModel> serdeTwitterAnalyticsAvroModel = new SpecificAvroSerde<>();
        serdeTwitterAnalyticsAvroModel.configure(serdeConfig, false);
        return serdeTwitterAnalyticsAvroModel;
    }

    private KStream<Long, TwitterAvroModel> getTwitterAvroModelKStream(Map<String, String> serdeConfig,
                                                                       StreamsBuilder streamsBuilder) {
        final Serde<TwitterAvroModel> serdeTwitterAvroModel = new SpecificAvroSerde<>();
        // Passamos false, que especifica que esse tipo não é uma chave.
        serdeTwitterAvroModel.configure(serdeConfig, false);
        // Retornamos um StreamsBuilder com o nome do Tópico de entrada e também passar a chave Long e
        // TwitterAvroModel para o valor.
        return streamsBuilder.stream(kafkaStreamsConfigData.getInputTopicName(), Consumed.with(Serdes.Long(),
                serdeTwitterAvroModel));
    }


}

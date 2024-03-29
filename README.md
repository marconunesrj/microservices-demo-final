# Running the application
- Please enter the correct credentials in twitter4j.properties file.
- Then run mvn install -DskipTests command
- Then go to docker-compose folder, execute o GITBASH, and run: chmod +x check-config-server-started.sh
- Then go to docker-compose folder, execute o GITBASH, and run: chmod +x check-kafka-topics-created.sh
- Then go to docker-compose folder, Execute: docker-compose --project-name event-drive-microservices up -d pois a referência dos arquivos .yml está no arquivo .env na variável COMPOSE_FILE
- Check the pom.xml file and spring-boot-maven-plugin section in twitter-to-kafka-service, where we configure 
the build-image goal to create docker image with mvn install command
- Check the services.yml file under docker-compose folder which includes the compose definition 
for microservice, twitter-to-kafka-service
- Then check the docker containers using docker ps command
- Use standalone kafkacat or docker container(https://hub.docker.com/r/confluentinc/cp-kafkacat) to install kafkacat
- Execute: docker pull confluentinc/cp-kafkacat
- Para verificar, execute: docker run -it --network=host confluentinc/cp-kafkacat:5.0.4 kafkacat -L -b localhost:19092
- Then check the kafka cluster

- Se estiver rodando no Docker
    docker run -it --network=host confluentinc/cp-kafkacat kafkacat -L -b localhost:19092

- Verificar os Topics que estão chegando no Kafka
   -C significa Consumers
   -t siginifica Topic
   docker run -it --network=host confluentinc/cp-kafkacat kafkacat -C -b localhost:19092 -t twitter-topic

# Confluentinc
- https://www.confluent.io/

# How to use kafkacat
- https://docs.confluent.io/platform/current/tools/kafkacat-usage.html


# Config Avro
- https://avro.apache.org/docs/1.11.1/getting-started-java/

## Criando a Classe Java a partir do modelo Avro
- Insert the configuration information on file pom.xml
- Create a model file .avro like: src.main.resources.avro.twitte.avsc
- Execute: mvn clean install

# Spring Data Elasticsearch
- https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/object-mapping.html
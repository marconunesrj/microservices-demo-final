#!/bin/bash

# Get the container name (optional, replace with your container name if known)
#container_name=$(docker ps --filter name=postgres --format "{{.Names}}")

# Check if container is running
#if [[ -z "$container_name" ]]; then
#  echo "Error: Could not find a running container named postgres."
#  exit 1
#fi

# Try connecting to PostgreSQL using pg_isready within the container
#docker exec keycloak_postgres pg_isready

# Check the exit code of the previous command (pg_isready)
#if [[ $? -eq 0 ]]; then
#  echo "PostgreSQL container '$container_name' is up and ready!"
#else
#  echo "Error: PostgreSQL within container '$container_name' is not ready."
#fi

#while [[ ! $? -eq 0 ]]; do
#  >&2 echo "Postgres server is not up yet!"
#  sleep 2
#  docker exec keycloak_postgres pg_isready
#done


# Função para verificar se o PostgreSQL está pronto para aceitar conexões
#function is_postgres_ready() {
#  # Tenta se conectar ao PostgreSQL usando o psql
#  pg_isready -h localhost -p 5442 > /dev/null 2>&1
#
#  # Retorna 0 se a conexão for bem-sucedida, 1 se não
#  return $?
#}

#while ! is_postgres_ready;  do
#  >&2 echo "Postgres server is not up yet!"
#  sleep 2
#done

# Installing netcat
#apt-get update && apt-get install -y netcat

#echo "Verifying if Postgres is ready."

#echo "Waiting for Postgres to start..."
#wait-for keycloak_postgres:5432
#
## Função para verificar se o PostgreSQL está pronto para aceitar conexões
#function is_postgres_ready() {
#  # Tenta se conectar ao PostgreSQL usando o psql
#  nc -z keycloak_postgres 5432 > /dev/null 2>&1
#
#  # Retorna 0 se a conexão for bem-sucedida, 1 se não
#  return $?
#}

#while ! is_postgres_ready;  do
#  >&2 echo "Postgres server is not up yet!"
#  sleep 2
#done

# Contador de tentativas
#tentativas=0

# https://www.certificacaolinux.com.br/comando-linux-netcat/
# Loop para verificar se o Postgres está pronto
#while ! nc -z keycloak_postgres 5432 > /dev/null 2>&1; do
#  # Aguarda 1 segundo antes de tentar novamente
#  sleep 1
#
#  # Incrementa o contador de tentativas
#  ((tentativas++))
#
#  # Se o número de tentativas for excedido, sai com erro
#  if [ "$tentativas" -ge 10 ]; then
#    echo "Erro: O Postgres não está pronto após 10 tentativas."
#    exit 1
#  fi
#done

#echo "O Postgres está pronto para conexões."

echo "Waiting for Postgres server is up!"
sleep 10
echo "Ok!"

/opt/keycloak/bin/kc.sh start-dev --http-port=9091
#./cnb/lifecycle/launcher
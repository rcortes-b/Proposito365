#!/bin/sh

./cleanup.sh

cd backend

mvn clean package -DskipTests

cd ../docker

docker compose up
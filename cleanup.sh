#!/bin/sh

docker stop propositos_mysql_container
docker rm propositos_mysql_container

docker stop propositos_backend_container
docker rm propositos_backend_container
docker image rm -f docker-backend

docker volume rm docker_db_data
docker network rm propositos_network
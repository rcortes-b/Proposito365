#!/bin/sh

docker stop propositos_mysql_container
docker rm propositos_mysql_container

docker stop propositos_backend_container
docker rm propositos_backend_container
docker image rm -f docker-backend

docker stop propositos_nginx_container
docker rm propositos_nginx_container
docker image rm -f docker-nginx_server

docker volume rm docker_db_data
docker network rm propositos_network
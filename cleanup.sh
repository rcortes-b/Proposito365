#!/bin/sh

docker stop propositos_mysql_container
docker rm propositos_mysql_container
docker volume rm docker_db_data
docker network rm docker_app_network
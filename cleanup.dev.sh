#!/bin/sh

docker stop dev_propositos_mysql_container
docker rm dev_propositos_mysql_container

docker volume rm docker_dev_db_data
docker network rm dev_propositos_network
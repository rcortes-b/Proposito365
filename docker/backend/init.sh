#!/bin/sh
echo "Waiting for MySQL..."
while ! (echo > /dev/tcp/db/3306) 2>/dev/null; do
  sleep 1
done
echo "MySQL is up!"
exec java -jar app.jar

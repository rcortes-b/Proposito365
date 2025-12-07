FROM amazoncorretto:21

COPY backend/target/app-0.0.1-SNAPSHOT.jar app.jar

COPY docker/backend/init.sh .
RUN chmod +x init.sh

ENTRYPOINT ["./init.sh"]
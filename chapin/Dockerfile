# Usa la imagen base de Amazon Corretto 17
FROM amazoncorretto:17-alpine

COPY target/chapin-0.0.1-SNAPSHOT.jar /app/chapin-0.0.1-SNAPSHOT.jar

WORKDIR /app

# Exponer el puerto que la aplicación utilizará
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "chapin-0.0.1-SNAPSHOT.jar"]
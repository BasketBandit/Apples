FROM openjdk:19-alpine

WORKDIR /app

COPY build/libs/apples-0.1.0.jar .
RUN chmod +x apples-0.1.0.jar

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "apples-0.1.0.jar"]
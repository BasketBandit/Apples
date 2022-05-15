FROM openjdk:17-alpine

WORKDIR /app
RUN mkdir data

COPY build/libs/apples-0.1.0.jar .
RUN chmod +x apples-0.1.0.jar

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "apples-0.1.0.jar"]
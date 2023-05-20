FROM eclipse-temurin:20-alpine

WORKDIR /app

COPY build/libs/booba-0.1.0.jar .
RUN chmod +x booba-0.1.0.jar

ENV DISCORD_BOT_TOKEN=$DISCORD_BOT_TOKEN

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "booba-0.1.0.jar"]
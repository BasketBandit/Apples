FROM openjdk:17-alpine

COPY entrypoint.sh /entrypoint.sh
CMD chmod +x entrypoint.sh

COPY build/libs/apples-0.1.0.jar /apples-0.1.0.jar
CMD chmod +x apples-0.1.0.jar

EXPOSE 443

ENTRYPOINT ["/entrypoint.sh"]
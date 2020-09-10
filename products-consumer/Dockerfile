FROM openjdk:11

ADD ./target/products-consumer-0.0.1-SNAPSHOT.jar /usr/src/products-consumer-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "products-consumer-0.0.1-SNAPSHOT.jar"]
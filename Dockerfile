FROM openjdk:11
EXPOSE 7000
ADD target/statistics-service.jar statistics-service.jar
ENTRYPOINT ["java","-jar","/statistics-service.jar"]
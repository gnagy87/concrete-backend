FROM openjdk:11
ADD target/concrete-test-app.jar concrete-test-app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "concrete-test-app.jar"]
FROM openjdk:11
ADD target/concrete-test-app.jar concrete-test-app.jar
EXPOSE 8086
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /concrete-test-app.jar"]

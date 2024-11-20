FROM openjdk:17-jdk-slim

COPY target/TaskManagementSystem-0.0.1-SNAPSHOT.jar /app/TaskManagementSystem-0.0.1-SNAPSHOT.jar

WORKDIR /app

CMD ["java", "-jar", "TaskManagementSystem-0.0.1-SNAPSHOT.jar"]
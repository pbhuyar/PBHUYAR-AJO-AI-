FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn clean install -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/Email-Template-Generator-0.0.1-SNAPSHOT.jar"]

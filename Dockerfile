FROM eclipse-tumurin:17-jdk-slim

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn clean install

EXPOSE 8080

CMD ["java", "-jar", "target/Email-Template-Generator-0.0.1-SNAPSHOT.jar"]

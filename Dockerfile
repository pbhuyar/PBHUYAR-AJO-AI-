FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

# 🔥 AUTO FIND POM FILE LOCATION
RUN find . -name "pom.xml"

# 🔥 BUILD FROM CORRECT LOCATION
RUN mvn -f $(find . -name pom.xml) clean install -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "java -jar $(find . -name '*.jar' | head -n 1)"]

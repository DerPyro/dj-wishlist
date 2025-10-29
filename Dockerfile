# Start mit offiziellem OpenJDK-Image
FROM eclipse-temurin:17-jre-alpine

# Arbeitsverzeichnis anlegen
WORKDIR /app

# JAR aus dem Build-Kontext kopieren
COPY target/dj-wishlist-*.jar app.jar

# Port freigeben (optional, z.B. 8080)
EXPOSE 8080

# Startbefehl
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:21

# Buat direktori kerja di dalam container
WORKDIR /app

# Salin file JAR ke dalam container
COPY build/libs/our-gameMarketPlaces-0.0.1-SNAPSHOT.jar /app/application.jar

# Jalankan aplikasi Java
CMD ["java", "-jar", "application.jar"]

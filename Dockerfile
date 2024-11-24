# Menggunakan Ubuntu sebagai base image
FROM openjdk:21

# Set JAVA_HOME environment variable (opsional)
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Buat direktori kerja di dalam container
WORKDIR /app

# Salin file JAR ke dalam container
COPY build/libs/our-gameMarketPlaces-0.0.1-SNAPSHOT.jar /app/application.jar

# Jalankan aplikasi Java
CMD ["java", "-jar", "application.jar"]

FROM mcr.microsoft.com/devcontainers/java:21

# Instalar Maven
ARG MAVEN_VERSION=3.9.6
RUN mkdir -p /usr/share/maven && \
    curl -fsSL https://apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# Configurar variables de entorno
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG /root/.m2

# Instalar herramientas adicionales
RUN apt-get update && apt-get install -y curl git wget unzip

# Limpiar caché
RUN apt-get clean && rm -rf /var/lib/apt/lists/*
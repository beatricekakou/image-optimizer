# Usa l'immagine ufficiale di Azure Functions per Java 17
FROM mcr.microsoft.com/azure-functions/java:4-java17 AS installer-env

RUN apt-get install -y maven

COPY . /build/java-function-app
RUN cd /build/java-function-app && \
    mkdir -p /home/site/wwwroot && \
    mvn clean package -Dmaven.test.skip=true && \
    cd ./target/azure-functions/ && \
    cd $(ls -d */|head -n 1) && \
    cp -a . /home/site/wwwroot

FROM mcr.microsoft.com/azure-functions/java:4-java17

RUN apt-get install -y webp

# Set environment variables for Azure Functions
ENV AzureWebJobsScriptRoot=/home/site/wwwroot \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true

# Add storage connection string for Blob Storage trigger
ENV AzureWebJobsStorage=""
# Copy the function app content from the installer stage
COPY --from=installer-env ["/home/site/wwwroot", "/home/site/wwwroot"]

EXPOSE 80

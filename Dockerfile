FROM maven:3-eclipse-temurin-21-alpine AS dependencies
WORKDIR /opt/app

COPY pom.xml .

RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=domain

FROM node:24-alpine AS build-ui

WORKDIR /opt/app

COPY . /opt/app

RUN npm install && \
    npm run build

FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /opt/app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY --from=build-ui /opt/app/ui/dist /opt/app/src/main/resources/static
COPY --from=build-ui /opt/app/node_modules/@salesforce-ux/design-system/assets/icons /opt/app/src/main/resources/static/assets/icons

COPY src /opt/app/src

RUN mvn -B -e clean package -DskipTests

FROM eclipse-temurin:21-alpine AS run

RUN apk update && \
    apk add tesseract-ocr tesseract-ocr-data-eng leptonica

WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar /app.jar
COPY --from=build /opt/app/src/main/resources src/main/resources
EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]


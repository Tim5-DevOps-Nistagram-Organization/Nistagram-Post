FROM maven:3.8.1-jdk-11 AS nistagramPostMicroserviceTest
ARG STAGE=test
WORKDIR /usr/src/server
COPY . .

FROM maven:3.8.1-jdk-11  AS nistagramPostMicroserviceBuild
ARG STAGE=dev
WORKDIR /usr/src/server
COPY . .
RUN mvn package -Pdev -DskipTests

FROM openjdk:11.0-jdk as nistagramPostMicroserviceRuntime
COPY --from=nistagramPostMicroserviceBuild /usr/src/server/target/*.jar nistagram-post.jar
CMD java -jar nistagram-post.jar

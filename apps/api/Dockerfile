FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /opt/app
COPY src ./src
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests

RUN jar xf target/api-0.0.1-SNAPSHOT.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 21  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    target/*.jar > deps.info
RUN jlink \
    --verbose \
    --add-modules $(cat deps.info),jdk.crypto.ec \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output /customjre

FROM eclipse-temurin:21-jre AS extractor
WORKDIR /opt/app
COPY --from=build /opt/app/target/api-0.0.1-SNAPSHOT.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM gcr.io/distroless/cc-debian12:nonroot
ENV JAVA_HOME=/opt/java/jdk21
ENV PATH=$JAVA_HOME/bin:$PATH
COPY --from=build /customjre $JAVA_HOME
WORKDIR /opt/app
COPY --from=extractor --chown=65532:65532 /opt/app/dependencies/ ./
COPY --from=extractor --chown=65532:65532 /opt/app/spring-boot-loader/ ./
COPY --from=extractor --chown=65532:65532 /opt/app/snapshot-dependencies/ ./
COPY --from=extractor --chown=65532:65532 /opt/app/application/ ./

USER nonroot

ENTRYPOINT ["java","-XX:+UseParallelGC","-XX:MaxRAMPercentage=75","org.springframework.boot.loader.launch.JarLauncher"]
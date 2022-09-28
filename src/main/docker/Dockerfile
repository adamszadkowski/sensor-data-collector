ARG JAVA_IMAGE=openjdk:17

FROM ${JAVA_IMAGE} as build

WORKDIR /src

COPY ./ /src

RUN microdnf install unzip findutils

# Build without integrationTest cause it uses testcontainers and required docker daemon
RUN ./gradlew clean build -x integrationTest && \
    unzip build/libs/sensor-data-collector.jar -d /app

FROM ${JAVA_IMAGE}

COPY --from=build /app/BOOT-INF/lib /app/lib
COPY --from=build /app/BOOT-INF/classes /app
COPY --from=build /app/META-INF /app/META-INF
COPY src/main/script/startup.sh /app/startup.sh

EXPOSE 80

VOLUME /tmp

CMD ["/app/startup.sh"]

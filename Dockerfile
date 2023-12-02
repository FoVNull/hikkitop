FROM eclipse-temurin:20-jre-alpine
ENV APP_DIR /app

WORKDIR ${APP_DIR}

ARG VERSION
ENV VERSION_ENV=${VERSION}

COPY libs/hikkitop.jar /app/hikkitop.jar

CMD ["java", "-jar", "/app/hikkitop.jar"]

EXPOSE 8080

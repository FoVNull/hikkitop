FROM eclipse-temurin:20-jre-alpine
ENV APP_DIR /app

WORKDIR ${APP_DIR}

RUN apk add --no-cache python3 py3-pip && \
    pip install pydub && \
    mkdir -p /app/tmp

ENV PYTHON=/usr/bin/python3

ARG VERSION
ENV VERSION_ENV=${VERSION}

COPY libs/hikkitop.jar /app/hikkitop.jar

CMD ["java", "-jar", "/app/hikkitop.jar"]

EXPOSE 8080

FROM eclipse-temurin:21-jre-alpine
ENV APP_DIR /

WORKDIR ${APP_DIR}

RUN apk add --no-cache python3 py3-pip && \
    pip install pydub

ENV PYTHON=/usr/bin/python3

ARG VERSION
ENV VERSION_ENV=${VERSION}

COPY libs/hikkitop.jar /app/hikkitop.jar
COPY AudioReverser.py /app/AudioReverser.py

CMD ["java", "-jar", "/app/hikkitop.jar"]

EXPOSE 8080

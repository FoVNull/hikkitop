FROM eclipse-temurin:21-jre-alpine
ENV APP_DIR /

WORKDIR ${APP_DIR}

RUN apk add --no-cache python3 py3-pip && \
    python3 -m venv /path/to/hikkitop && \
    . /path/to/hikkitop/bin/activate && \
    pip install pydub

ENV PATH="/path/to/hikkitop/bin:$PATH"

ARG VERSION
ENV VERSION_ENV=${VERSION}

COPY libs/hikkitop.jar /app/hikkitop.jar
COPY AudioReverser.py /app/AudioReverser.py

CMD ["java", "-jar", "/app/hikkitop.jar"]

EXPOSE 8080

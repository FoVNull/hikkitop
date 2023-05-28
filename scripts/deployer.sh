cd /app
nohup java -jar /app/hikkitop.jar > /app/toplogs/backend.log

cd /app/hikkitop
nohup ./gradlew frontendRun -t > /app/toplogs/frontend.log
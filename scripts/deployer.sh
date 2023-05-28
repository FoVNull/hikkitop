chmod 777 /app/hikkitop/gradlew

echo "Deploy frontend"
nohup ./gradlew frontendRun -t > /app/toplogs/frontend.log

echo "pending..."
sleep 60s

echo "Deploy backend"
cd /app
nohup java -jar /app/hikkitop.jar > /app/toplogs/backend.log

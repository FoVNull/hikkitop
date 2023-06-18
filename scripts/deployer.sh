chmod 777 /app/hikkitop/gradlew

cd /app/hikkitop;
git reset --hard HEAD
git pull
sleep 5s

echo "Deploy frontend"
nohup ./gradlew frontendRun -t > /app/toplogs/frontend.log

echo "pending..."
sleep 60s

echo "Deploy backend"
cd /app
nohup java -jar /app/hikkitop.jar > /app/toplogs/backend.log


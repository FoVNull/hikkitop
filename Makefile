stop_and_remove:
	docker stop hikkitop_jar || true
	docker rm hikkitop_jar || true

run_prod: stop_and_remove
	docker run -d -p 80:8080 --name hikkitop_jar -e HIKKI_KEY=/app/key.yml -v /app/tmp:/app/tmp hypingdog/hikkitop:1.2.1
	docker cp /app/key.yml hikkitop_jar:/app

stop_and_remove_dev:
	docker stop hikkitop_jar_dev || true
	docker rm hikkitop_jar_dev || true

run_dev: stop_and_remove_dev
	docker run -d -p 2233:8080 --name hikkitop_jar_dev -e HIKKI_KEY=/app/key.yml -v /app/tmp:/app/tmp hypingdog/hikkitop:dev
	docker cp /app/key.yml hikkitop_jar_dev:/app

remove_unused_image:
	docker image prune

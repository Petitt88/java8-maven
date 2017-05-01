
To use it with docker:
	docker run -p 5000:80 -d --name web_1 webpythonredis_web
	docker stop web_1
	docker start web_1
	docker rm web_1

	
To use it with docker-compose:
	docker-compose up
	docker-compose down
	docker-compose scale redis=4

To use it with docker in production (deploy):
	docker swarm init - to enable swarm mode and make your current machine a swarm manager
	docker stack deploy -c docker-compose.deploy.yml getstartedlab - run the app with the name "getstartedlab"
	docker stack ps getstartedlab - list the running containers
		You can scale the app by changing the replicas value in docker-compose.yml, saving the change, and re-running the docker stack deploy command:	docker stack deploy -c docker-compose.deploy.yml getstartedlab
	docker stack rm getstartedlab - take down the app
	
	
The difference between docker-compose.yml and docker-compose.deploy.yml is that the former is meant to be used with "docker-compose" commands and the latter is with "docker stack deploy".
"docker-compose" does not understand deploy-related settings like the number of instances to start, maximum allowed CPU, RAM to use.
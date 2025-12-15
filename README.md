# Proposito365

## Docker

Using docker compose to deploy:
	- Front
	- Backend
	- Database -> Dockerfile not necessary, directly from docker-compose file

### Database
Automatically deployed with docker-compose. User setup + db using `env_file:` property

### Backend
#### NOTE  ####
application.properties of springboot has the db user & pass as environment variables. The idea is to pass .env also to the backend container 

### Methods
	- Roles && Status -> findById
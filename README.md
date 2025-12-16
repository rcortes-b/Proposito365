# Proposito365

## Change before go prod
	- SSL certificates --> Change the CN for the IP/Domain

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



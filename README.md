# Requirements
- Java 21
- docker
- docker-compose

# First run the following commands in the root of the project
```shell
./mvnw clean install -DskipTests
docker build -t user-projects:latest .
docker-compose up
```
- This will start the app, the db, and an extra db exclusively for the tests.

# To run the tests
```shell
./mvnw test
```

# Some curl examples 
```shell
#create a user
curl --header "Content-Type: application/json"   --request POST   --data '{"email":"user1@gmail.com","password": "12345678", "name" : "User One"}' http://localhost:8080/users/

#set variable, use the id that you obtain in the response of the user creation
USER_ID={THE_ID_RECEIVED_IN_PREVIOUS_RESPONSE)

#get user
curl -u "user1@gmail.com:12345678" --header "Content-Type: application/json"   --request GET  http://localhost:8080/users/${USER_ID}/

#add external project to user
curl -u "user1@gmail.com:12345678" --header "Content-Type: application/json"   --request PATCH --data '{"externalProjectId":"external-id-1"}' http://localhost:8080/users/${USER_ID}/external-projects/

#get user with external projects
curl -u "user1@gmail.com:12345678" --header "Content-Type: application/json"   --request GET  http://localhost:8080/users/${USER_ID}/external-projects/

# delete user
curl -u "user1@gmail.com:12345678" --header "Content-Type: application/json"   --request DELETE http://localhost:8080/users/${USER_ID}/
```

# Other notes and assumptions
- it was used basic auth, where the username is the email of the user.
- for deletions it was implemented a real delete, not a logic one.
- it was assumed that a normal user should only be able to delete himself.
- it was assumed that a normal user should only be able to add external projects to himself.
- i had no time to implement roles for the user, so for the sake of the exercise, considered the user with email "thesuperadmin@company.com" as a SUPER_ADMIN with possibility to perform any available operation over any user.
- it was used a couple of jakarta.validation.constraints to validate the input in the requests.
- the exception handling and proper error responses are handled in "ApiExceptionHandler" class.
- the endpoint to create a user has no authentication.
- the only sql script is in /init-db/schema.sql and it runs automatically when starting the db containers.


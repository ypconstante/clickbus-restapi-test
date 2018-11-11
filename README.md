# How to use?

Follow steps below to run the API.

1. Create a postgres database
2. Configure the environment variables to API connect on database, they are: database name ${CLICKBUSDB}, db user ${POSTGRESUSER} and db password ${POSTGRESPASSWORD}.
You can change this configuration in file application.properties.
3. Build the project with the maven command 'mvn clean install' this will generate a executable jar '\target\restapi-0.0.1-SNAPSHOT.jar'
4. Start the API with the command 'java -jar restapi-0.0.1-SNAPSHOT.jar'
5. Access the API on link localhost:8080 you can use Swagger on link localhost:8080/swagger-ui.html

You can run test with 'mvn test'
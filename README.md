# **Welcome to Amazing Volunteer!**

**Description**
**Tools**
 - Spring Boot
 - Postgresql 15
 - pg admin4
## **Setup**
### Environment setup:
- Setup Java SDK: Download Java SDK [here](https://www.oracle.com/java/technologies/downloads/)
- Setup maven: follow step in the docs (Remember follow 
 setup in README) 
- Required setup: - Java JDK: 11 - Apache Maven: 3.6.3 - 
  Postgresql: 15

### Environment variables setup:
- Create env.properties file in `src/main/resources`
- And go to the file then set variable as your Jotform API key, for example:
  ```API_KEY=03bd298e48dkde5140e32c773e28c555```
- Set variables in application.properties, example: 
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/amazing-volunteer
  spring.datasource.username=postgres
  spring.datasource.password=postgres
  ```

### Run project

  After setup everything for development you just need to run:
  $ `mvn spring-boot:run`

## **API Documentation**
http://localhost:8080/api/swagger-ui/index.html
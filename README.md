# CreditCardApplication
Credit Card Management Application

### Prerequsites
1. The latest versions of **Java11** and **Gradle** must be installed on your system. 
    

### Run Server
1. Clone the source code from github
 
 
2. Build the project with the following command in the terminal
 
 ./gradlew build
 
 3. Create the docker image with the next command:
 
 docker build -t creditcard-app .
 
 4. Then run the container:
 
 docker run -p 8080:8080 -t creditcard-app
 
 5. Swagger: https://github.com/gibykthomas/CreditCardApplication/blob/main/swagger/swagger.yml
```

# NStory Web Project
NStory project using the Spring Boot framework.

## Getting Started
### Prerequisites
To run this project, you will need to have the following installed:

- Java 17
- Gradle
- MySQL or MariaDB

### Installation
1. Clone the repository:
    ```shell
    git clone https://github.com/Jwdev-k/NStory.git
    ```
2. Create a MariaDB database, SSL and update the application.properties    
file with your database information:
    ```properties
    spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    spring.datasource.url=jdbc:mariadb://localhost:3306/exampleDB
    spring.datasource.username=root
    spring.datasource.password=password

    server.ssl.key-store=classpath:example.p12
    server.ssl.key-store-type=PKCS12
    server.ssl.key-store-password=password
    ```
3. Build the project using Gradle:
    ```shell
    cd NStory
    gradle clean build
    ```
4. Run the project:   
Gradle:
    ```shell
    cd NStory
    gradle bootRun
    ```
# :paperclip: NStory Web Project
> NStory는 커뮤니티 사이트의 기본이되는 기능은 모두 담겨있는 프로젝트입니다.

### 📝사용기술
#### 1. 백엔드
#### 📗 주요 프레임워크 / 라이브러리
- Java 17
- SpringBoot 3.0.5
- Spring Security
- Mybatis framework
- OAuth 2.0
- WebSocket
#### ⚙ 템플릿 엔진
- thymeleaf
#### 🔨 Build Tools
- Gradle 7.6.x
#### 📚 DataBase
- MariaDB

#### 2. 프론트엔드
- HTML/CSS
- BootStrap
- JavaScript
- Summernote

### 💡 설치방법
1. repository 클론방법:
    ```shell
    git clone https://github.com/Jwdev-k/NStory.git
    ```
2. MariaDB or MySQL 데이터베이스 생성 및 SSL 추가, application.properties 파일에    
데이터베이스 및 SSL인증서 정보에 맞게 수정:
    ```properties
    spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
    spring.datasource.url=jdbc:mariadb://localhost:3306/exampleDB
    spring.datasource.username=root
    spring.datasource.password=password

    server.ssl.key-store=classpath:example.p12
    server.ssl.key-store-type=PKCS12
    server.ssl.key-store-password=password
    ```
4. Gradle 프로젝트 빌드:
    ```shell
    cd NStory
    gradle clean build
    ```
5. 프로젝트 실행:   
Gradle:
    ```shell
    cd NStory
    gradle bootRun
    ```
   
### 📷 메인화면
<img src="https://user-images.githubusercontent.com/82058641/229183420-6dba2e72-0a0e-48bc-be7b-2b1b7ddbcf37.png">
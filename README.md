# :paperclip: NStory Web Project

## 소개
이 프로젝트는 사용자 로그인, 회원가입, 게시판 기능, 라이브 스트리밍 등을 제공하는 웹 애플리케이션입니다. 각종 카테고리에 맞춘 게시판 및 사용자 관리 기능과 실시간 채팅, 영상 스트리밍까지 지원합니다.

## 📺주요 기능 (Feature)

### 1. 로그인 화면
- 기본 로그인 및 SNS 로그인(API 통합) 기능
- Google reCAPTCHA 적용으로 보안 강화

### 2. 회원가입
- 이메일 인증을 통한 회원가입 절차
- 기존에 가입된 이메일인지 확인하는 기능
- SNS 로그인(API)을 통한 자동 회원가입 기능
- Google reCAPTCHA 적용으로 보안 강화

### 3. 사용자 관리
- 각 사용자는 레벨, 경험치, 코인 기능을 보유
- 사용자 페이지에서 다음 기능을 이용 가능:
   - 회원 탈퇴
   - 패스워드 변경
   - 코멘트 수정
   - 게시글 작성 목록 및 댓글 작성 목록 확인

### 4. 방명록
- 간단한 카드 형식으로 짧은 글을 작성하는 방명록 페이지 제공

### 5. 게시판 기능
- 글 작성, 수정, 삭제 기본 기능 제공
- 댓글, 대댓글 작성 가능
- 게시글 좋아요/싫어요 기능
- 다양한 카테고리에 맞춘 여러 개의 게시판 생성 가능
- 각 게시판별 대표 이미지 및 설명 변경 가능
- 게시판 관리자 등급을 통해 해당 게시판 관리자가 공지사항 작성 및 설정 변경 가능

### 6. 라이브 채팅 및 스트리밍
- 실시간 채팅 및 라이브 스트리밍 기능 제공
- FFmpeg를 이용한 영상 데이터 가공
- HLS 기술을 통해 스트리밍 제공
- 기본 로직은 구현되어 있으며, OBS와 같은 외부 프로그램을 사용해야 함

## 📝구성 요소

#### 1. 백엔드

#### 📗 주요 프레임워크 / 라이브러리

- JDK 21
- SpringBoot 3.3.3
- Spring Security
- Mybatis framework
- OAuth 2.0
- WebSocket
- SMTP

#### ⚙ 템플릿 엔진

- thymeleaf

#### 🔨 Build Tools

- Gradle 8.6

#### 📚 DataBase

- MariaDB

#### 2. 프론트엔드

- HTML/CSS
- BootStrap
- JavaScript
- Summernote
- JQuery

---

### 💡 (Gradle) 기본 설치방법

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
3. Gradle 프로젝트 빌드:

   ```shell
   cd NStory
   gradle clean build
   ```
4. Gradle 프로젝트 실행:

   ```shell
   cd NStory
   gradle bootRun
   ```

---

### 🧾 Database(ERD)

<img src="https://user-images.githubusercontent.com/82058641/235068176-3c02f1ef-a3bb-4d65-9362-f795da95443a.PNG" alt="ERD"/>
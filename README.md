# 🍃 스프링 학습 프로젝트
> **스프링**의 핵심 원리를 적용해서 학습하는 프로젝트입니다.

---

## 📝 학습 기록
```
[o] Swagger
[o] Security
[o] JPA
[o] MySql
[o] thymeleaf
[o] 테스트 코드 작성법
[o] 예외 처리 전략
[ ] 오브젝트와 의존관계 (IoC, DI)
[ ] 템플릿과 콜백
[ ] 서비스 추상화 및 트랜잭션 (AOP)
```

---

## 🛠 기술 스택 (Tech Stack)

| 구분 | 기술 스택 | 버전 |
| :--- | :--- | :--- |
| **Language** | Java | 17 (OpenJDK 17.0.12) |
| **Framework** | Spring Boot | 3.4.1 |
| **Database** | MySQL | 9.1.0 |
| **ORM** | Spring Data JPA | - |
| **Build Tool** | Apache Maven | 3.8.1 |
| **API Docs** | Springdoc-OpenAPI (Swagger) | 2.8.4 |

---

## 📦 프로젝트 구조 (Project Structure)

```text
├── src
│   ├── main
│   │   ├── java            # 백엔드 비즈니스 로직
│   │   └── resources       # 설정 파일 (application.yml, SQL 등)
│   │       └── static
│   │           └── css     # css 파일
│   │       └── templates   # thymeleaf 파일
│   └── test
│       └── java            # 단위 및 통합 테스트 코드
├── docs                    # API 명세서 및 학습 정리 문서
└── pom.xml                 # Maven 의존성 관리
```
---

## 📦 설정 및 실행 방법 (Configuration & Setup)

1. Database 설정 (MySQL) : 로컬에 MySQL이 설치되어 있어야 하며, 아래의 데이터베이스를 생성해야 합니다.
```
create table springbook.user (
    id BIGINT primary key,
    name varchar(20) not null,
    password varchar(20) not null
);
select * from springbook.user;
show databases ;
use springbook;
use mysql;
use root;
SELECT VERSION();
select user, host from user;
grant all privileges on *.* to 'spring'@'book';
SELECT user, host FROM mysql.user WHERE user = 'spring';
GRANT ALL PRIVILEGES ON springbook.* TO 'spring'@'%';
FLUSH PRIVILEGES;

ALTER TABLE springbook.users MODIFY COLUMN password varchar(20) NOT NULL;
commit ;
SHOW GLOBAL VARIABLES LIKE 'PORT';
DROP TABLE springbook.user;
DESC springbook.user;
ALTER TABLE springbook.user MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE springbook.user ADD level tinyint NOT NULL;
ALTER TABLE springbook.user ADD login int NOT NULL;
ALTER TABLE springbook.user ADD recommend int NOT NULL;
ALTER TABLE springbook.user ADD email varchar(100) NOT NULL;
```


2. application.yml 설정 : src/main/resources/application.properties 파일을 수정합니다.


3. 실행 및 API 문서 확인
```
1) SpringOfTobyBootApplication.java 실행

2) 브라우저에서 Swagger UI 접속     : http://localhost:8080/swagger-ui/index.html
```

## 🛠 주요 라이브러리 (Dependencies)
```
프로젝트의 핵심 의존성 구성입니다. (pom.xml 기준)
 - Spring Boot Starter Web: RESTful API 개발
 - Spring Boot Starter Data JPA: 데이터베이스 영속성 관리
 - MySQL Connector Java: MySQL 연동
 - Springdoc OpenAPI Starter: API 문서 자동화
 - Spring Boot Starter Security: (필요 시) 보안 및 인증 관리
```
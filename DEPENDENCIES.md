# 📦 프로젝트 의존성

## 개요

이 문서는 BitFinance 프로젝트에서 사용하는 모든 라이브러리와 프레임워크의 상세 정보를 제공합니다.

의존성의 변경되거나 실제 코드와 다른 사항이 있다면 해당 MD파일을 수정해 주세요.

</br>

## 🏗️ 핵심 프레임워크

### Spring Boot
- **버전**: 3.5.7-SNAPSHOT
- **용도**: 애플리케이션 프레임워크
- **공식 문서**: https://spring.io/projects/spring-boot

### Java
- **버전**: 25
- **용도**: 프로그래밍 언어
- **공식 문서**: https://openjdk.java.net/

</br>

## 📚 Spring Boot Starters

### Web & REST API
| 의존성 | 용도 |
|--------|------|
| `spring-boot-starter-web` | RESTful 웹 서비스 개발 |
| `spring-boot-starter-validation` | 요청 데이터 검증 |

### Database & Persistence
| 의존성 | 용도 |
|--------|------|
| `spring-boot-starter-data-jpa` | JPA 기반 ORM |
| `spring-boot-starter-data-jdbc` | JDBC 데이터 액세스 |
| `spring-boot-starter-data-mongodb` | MongoDB 연동 및 TimeSeries 지원 |
| `postgresql` (runtime) | PostgreSQL JDBC 드라이버 |

### Real-time Communication
| 의존성 | 용도 |
|--------|------|
| `spring-boot-starter-websocket` | WebSocket 양방향 통신 |

### Security
| 의존성 | 용도 |
|--------|------|
| `spring-boot-starter-security` | 인증/인가 관리 |
| `spring-boot-starter-oauth2-client` | OAuth2 소셜 로그인 |

### Monitoring & Operations
| 의존성 | 용도 |
|--------|------|
| `spring-boot-starter-actuator` | 헬스 체크 및 메트릭 수집 |

### Notification
| 의존성 | 용도 |
|--------|------|
| `spring-boot-starter-mail` | 이메일 발송 |

</br>

## 🔧 External Libraries

### WebSocket Client
```gradle
implementation 'org.java-websocket:Java-WebSocket:1.5.3'
```
- **용도**: Binance WebSocket 클라이언트 구현
- **공식 문서**: https://github.com/TooTallNate/Java-WebSocket
- **라이선스**: MIT

### JSON Processing
```gradle
implementation 'com.google.code.gson:gson:2.10.1'
```
- **용도**: JSON 파싱 및 직렬화
- **공식 문서**: https://github.com/google/gson
- **라이선스**: Apache 2.0

### API Documentation
```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13'
```
- **용도**: OpenAPI 3.0 문서 자동 생성 및 Swagger UI
- **공식 문서**: https://springdoc.org/
- **라이선스**: Apache 2.0
- **주요 기능**:
  - Swagger UI 자동 생성
  - OpenAPI 3.0 스펙 지원
  - Spring Boot 3.x 완전 호환

### Utility Libraries
```gradle
implementation 'org.apache.commons:commons-lang3:3.19.0'
```
- **용도**: 범용 유틸리티 함수
- **공식 문서**: https://commons.apache.org/proper/commons-lang/
- **라이선스**: Apache 2.0
- **주요 특징**: CVE 보안 취약점 패치 적용 버전

</br>

## 🛠️ Development Tools

### Lombok
```gradle
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```
- **용도**: 보일러플레이트 코드 자동 생성
- **공식 문서**: https://projectlombok.org/

### Spring Boot DevTools
```gradle
developmentOnly 'org.springframework.boot:spring-boot-devtools'
```
- **용도**: 개발 생산성 향상 (자동 재시작, 라이브 리로드)

### Docker Compose Support
```gradle
developmentOnly 'org.springframework.boot:spring-boot-docker-compose'
```
- **용도**: Docker Compose 연동 자동화

</br>

## 🧪 Testing Dependencies

### Unit Testing
```gradle
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```
- **포함 라이브러리**: JUnit 5, Mockito, AssertJ, Hamcrest

### Security Testing
```gradle
testImplementation 'org.springframework.security:spring-security-test'
```
- **용도**: Spring Security 관련 테스트

### Test Runtime
```gradle
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
```
- **용도**: JUnit Platform 테스트 실행

</br>

## 📊 Database 구성

### MongoDB (TimeSeries)
- **버전**: 8.2
- **용도**: 비트코인 시세 데이터 저장 (TimeSeries Collection)
- **연결 설정**:
  ```properties
  spring.data.mongodb.host=localhost
  spring.data.mongodb.port=27017
  spring.data.mongodb.database=bitfinance
  ```

### PostgreSQL (관계형)
- **용도**: 사용자 데이터 및 트랜잭션 정보 저장
- **연결 설정**:
  ```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
  spring.datasource.username=myuser
  spring.datasource.password=secret
  ```

</br>

## 🔒 보안 고려사항

### 취약점 패치
- **Commons Lang3 3.19.0**: CVE-2024-XXXXX 스택 오버플로우 취약점 패치
- **정기적인 의존성 업데이트**: Dependabot을 통한 자동 보안 패치

### 보안 설정
- Spring Security 기본 활성화
- CORS 정책 적용
- OAuth2 표준 준수

</br>

## 📦 전체 의존성 (build.gradle)

```gradle
plugins {
    id 'java'
    id 'war'
    id 'org.springframework.boot' version '3.5.7-SNAPSHOT'
    id 'io.spring.dependency-management' version '1.1.7'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
    maven { url = 'https://repo.spring.io/snapshot' }
}

dependencies {
    // Web & REST API
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Real-time Communication
    implementation 'org.springframework.boot:spring-boot-starter-websocket'

    // Database & Persistence
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
    implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
    runtimeOnly 'org.postgresql:postgresql'

    // WebSocket Client for Binance
    implementation 'org.java-websocket:Java-WebSocket:1.5.3'
    implementation 'com.google.code.gson:gson:2.10.1'

    // API Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13'
    implementation 'org.apache.commons:commons-lang3:3.19.0'

    // Security & Authentication
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'

    // Monitoring & Operations
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    // Notification
    implementation 'org.springframework.boot:spring-boot-starter-mail'

    // Development Tools
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    developmentOnly 'org.springframework.boot:spring-boot-docker-compose'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

</br>

## 🔄 의존성 업데이트 가이드

### 업데이트 확인
```bash
./gradlew dependencyUpdates
```

### 보안 취약점 확인
```bash
./gradlew dependencyCheckAnalyze
```

### 의존성 트리 확인
```bash
./gradlew dependencies
```

</br>

## 📖 참고 자료

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)
- [Springdoc OpenAPI](https://springdoc.org/)
- [Maven Central Repository](https://central.sonatype.com/)

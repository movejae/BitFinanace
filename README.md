# 💰 BitFinance - Real-time Cryptocurrency Asset Management Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://openjdk.java.net/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> 실시간 암호화폐 및 디지털 자산 정보를 수집, 저장, 분석하여 사용자에게 제공하는 엔터프라이즈급 백엔드 시스템

</br>

## 📋 프로젝트 개요

BitFinance는 비트코인을 비롯한 다양한 암호화폐 자산의 실시간 시세 정보를 외부 API를 통해 수집하고, 이를 체계적으로 데이터베이스에 저장하여 사용자에게 RESTful API로 제공하는 백엔드 플랫폼입니다. Spring Boot 기반의 현대적인 아키텍처와 WebSocket을 활용한 실시간 데이터 스트리밍을 지원합니다.

### 🎯 주요 기능

- **실시간 자산 데이터 수집**: 외부 암호화폐 거래소 API와 연동하여 실시간 시세 정보 수집
- **데이터 영속화**: PostgreSQL 기반의 안정적인 데이터 저장 및 히스토리 관리
- **RESTful API 제공**: Spring Data REST를 활용한 표준화된 API 엔드포인트
- **실시간 알림**: WebSocket 기반의 실시간 시세 변동 알림 시스템
- **보안 인증**: OAuth2 + Spring Security 기반의 안전한 사용자 인증/인가
- **시스템 모니터링**: Spring Boot Actuator를 통한 애플리케이션 헬스 체크 및 메트릭 수집

</br>
  

## 🛠️ Git Commit Convention

이 프로젝트는 일관성 있는 커밋 히스토리 관리를 위해 Conventional Commits 규칙을 따릅니다.

```bash
# 예시
feat(api): 비트코인 시세 조회 API 추가
fix(websocket): 연결 끊김 오류 수정
docs(readme): 설치 가이드 업데이트
```

자세한 커밋 규칙은 **[COMMIT_CONVENTION.md](./COMMIT_CONVENTION.md)** 문서를 참고해주세요.

</br>

## ❤️Coding Standard

이 프로젝트는 도메인 주도 설계(DDD)와 안전한 객체 생성 원칙을 따릅니다.

**핵심 원칙:**
- ✅ Builder 패턴으로 필수 필드 보장 (필수값 누락 시 런타임 에러 방지)
- ✅ String 파라미터 2개 이상 시 DTO/Command 객체로 묶기
- ✅ 최대한 재사용되는 로직만 메서드로 분리 (불가피한 경우 제외)
- ✅ Stream API 적극 활용 (for문 대신 선언적 코드)
- ✅ 복잡한 람다 표현식은 메서드로 추출 (가독성 우선)
- ✅ 도메인 로직은 엔티티에 위치

자세한 코딩 규칙은 **[CODING_STANDARD.md](./CODING_STANDARD.md)** 문서를 참고해주세요.

</br>

## 🏗️ 기술 스택

### Backend Framework
- **Spring Boot 3.5.7** - 최신 Spring Boot 프레임워크
- **Java 24** - 최신 Java LTS 버전 사용

### 핵심 기술
| 기술 | 용도 | 설명 |
|------|------|------|
| Spring Data JPA | ORM | 데이터베이스 엔티티 관리 및 쿼리 추상화 |
| Spring Data JDBC | Data Access | 경량 데이터 액세스 레이어 |
| Spring Data REST | API | HATEOAS 기반 RESTful API 자동 생성 |
| Spring Security | 보안 | 인증/인가 및 보안 정책 관리 |
| OAuth2 Client | 인증 | 소셜 로그인 및 외부 API 인증 |
| Spring WebSocket | 실시간 통신 | 양방향 실시간 데이터 스트리밍 |
| Spring Validation | 유효성 검증 | 요청 데이터 검증 및 에러 핸들링 |
| Spring Boot Actuator | 모니터링 | 애플리케이션 상태 모니터링 및 메트릭 |
| Spring Mail | 알림 | 이메일 기반 알림 시스템 |

### Database & Infrastructure
- **PostgreSQL** - 트랜잭션 처리 및 데이터 무결성 보장
- **Docker Compose** - 컨테이너 기반 개발 환경 구성
- **Lombok** - 보일러플레이트 코드 최소화
- **Spring Boot DevTools** - 개발 생산성 향상

### Build & Test
- **Gradle** - 의존성 관리 및 빌드 자동화
- **JUnit 5** - 단위 테스트 프레임워크
- **Spring Security Test** - 보안 테스트 도구

</br>

## 📦 의존성 구조

```gradle
dependencies {
    // Web & REST API
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-rest'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Real-time Communication
    implementation 'org.springframework.boot:spring-boot-starter-websocket'

    // Database & Persistence
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
    runtimeOnly 'org.postgresql:postgresql'

    // Security & Authentication
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'

    // Monitoring & Operations
    implementation 'org.springframework.boot:spring-boot-starter-actuator'

    // Notification
    implementation 'org.springframework.boot:spring-boot-starter-mail'

    // Development Tools
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    developmentOnly 'org.springframework.boot:spring-boot-docker-compose'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
```

</br>

## 🚀 시작하기

### 사전 요구사항

- Java 24 이상
- Docker & Docker Compose
- Gradle 8.x
- PostgreSQL (Docker 사용 시 불필요)

### 설치 및 실행

1. **저장소 클론**
   ```bash
   git clone https://github.com/yourusername/BitFinanace.git
   cd BitFinanace
   ```

2. **데이터베이스 실행**
   ```bash
   docker-compose up -d
   ```

3. **애플리케이션 빌드**
   ```bash
   ./gradlew clean build
   ```

4. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

5. **애플리케이션 접속**
   ```
   http://localhost:8080
   ```

### 환경 설정

`src/main/resources/application.properties` 파일에서 다음 설정을 구성합니다:

```properties
# Application Name
spring.application.name=BitFinanace

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Actuator Endpoints
management.endpoints.web.exposure.include=health,metrics,info
```

</br>

## 📊 시스템 아키텍처

```
┌─────────────────────────────────────────────────────────┐
│                     External APIs                        │
│         (Binance, Coinbase, CoinGecko, etc.)           │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              BitFinance Backend Service                  │
│  ┌────────────────────────────────────────────────┐    │
│  │         WebSocket Real-time Layer              │    │
│  └────────────────────────────────────────────────┘    │
│  ┌────────────────────────────────────────────────┐    │
│  │      RESTful API Layer (Spring Data REST)     │    │
│  └────────────────────────────────────────────────┘    │
│  ┌────────────────────────────────────────────────┐    │
│  │   Security Layer (OAuth2 + Spring Security)   │    │
│  └────────────────────────────────────────────────┘    │
│  ┌────────────────────────────────────────────────┐    │
│  │       Business Logic Layer (Services)          │    │
│  └────────────────────────────────────────────────┘    │
│  ┌────────────────────────────────────────────────┐    │
│  │   Data Access Layer (Spring Data JPA/JDBC)    │    │
│  └────────────────────────────────────────────────┘    │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              PostgreSQL Database                         │
│    (Asset Data, User Data, Transaction History)        │
└─────────────────────────────────────────────────────────┘
```

</br>

## 🔑 핵심 기능 상세

### 1. 실시간 데이터 수집 시스템
- 스케줄러 기반 주기적 데이터 수집
- 외부 API Rate Limit 관리
- 에러 핸들링 및 재시도 메커니즘

### 2. RESTful API 엔드포인트
```
GET    /api/assets           - 전체 자산 목록 조회
GET    /api/assets/{id}      - 특정 자산 상세 조회
GET    /api/assets/search    - 자산 검색
POST   /api/assets           - 새 자산 등록
PUT    /api/assets/{id}      - 자산 정보 수정
DELETE /api/assets/{id}      - 자산 삭제
```

### 3. WebSocket 실시간 스트리밍
```javascript
// 클라이언트 연결 예시
const socket = new WebSocket('ws://localhost:8080/ws/assets');
socket.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('실시간 시세:', data);
};
```

### 4. 보안 기능
- JWT 기반 토큰 인증
- OAuth2 소셜 로그인 (Google, GitHub 등)
- CORS 정책 관리
- API Rate Limiting

## 📈 모니터링 및 운영

### Actuator 엔드포인트

- `GET /actuator/health` - 애플리케이션 상태 확인
- `GET /actuator/metrics` - 시스템 메트릭 조회
- `GET /actuator/info` - 애플리케이션 정보

### 로깅

```properties
logging.level.com.finpuff.bitfinanace=DEBUG
logging.level.org.springframework.web=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

</br>

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests BitFinanaceApplicationTests

# 테스트 커버리지 리포트 생성
./gradlew jacocoTestReport
```

</br>


## 📝 개발 로드맵

- [x] 프로젝트 초기 구조 설정
- [x] Spring Boot 기반 아키텍처 구축
- [ ] 외부 암호화폐 API 연동 (Binance, CoinGecko)
- [ ] 실시간 데이터 수집 스케줄러 구현
- [ ] RESTful API 엔드포인트 개발
- [ ] WebSocket 실시간 스트리밍 구현
- [ ] OAuth2 소셜 로그인 구현
- [ ] 데이터 분석 및 차트 API
- [ ] 알림 시스템 (이메일, 푸시)
- [ ] 관리자 대시보드
- [ ] Docker 배포 최적화
- [ ] Kubernetes 오케스트레이션
- [ ] CI/CD 파이프라인 구축


</br>

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (커밋 컨벤션을 따라주세요)
   - **[커밋 컨벤션 가이드 보기](./COMMIT_CONVENTION.md)**
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


</br>

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

</br>

## 👤 개발자

**Finpuff Team**

- GitHub: [@movejae](https://github.com/movejae)
- Email: contact@finpuff.com

## 🙏 감사의 말

- [Spring Framework](https://spring.io/)
- [CoinGecko API](https://www.coingecko.com/en/api)
- [Binance API](https://binance-docs.github.io/apidocs/)

---

⭐️ 이 프로젝트가 도움이 되셨다면 Star를 눌러주세요!

ㅁ# 🚀 설치 및 실행 가이드

## 개요

이 문서는 BitFinance 프로젝트를 로컬 환경에서 설치하고 실행하는 방법을 안내합니다.

</br>

## ⚙️ 사전 요구사항

### 필수 요구사항

- **Java 25 이상**
  - [OpenJDK 다운로드](https://openjdk.java.net/)
  - 설치 확인: `java -version`

- **MongoDB 8.2 이상**
  - TimeSeries Collection 지원 필요
  - [MongoDB 다운로드](https://www.mongodb.com/try/download/community)

- **Gradle 8.x**
  - 프로젝트에 포함된 Gradle Wrapper 사용 가능

### 선택 사항

- **Docker & Docker Compose** - 컨테이너 기반 개발 환경
- **PostgreSQL** - 사용자 데이터 저장용 (향후 사용 예정)

</br>

## 📥 설치 단계

### 1. 저장소 클론

```bash
git clone https://github.com/yourusername/BitFinanace.git
cd BitFinanace
```

</br>

### 2. MongoDB 설치 및 실행

#### macOS (Homebrew 사용)

```bash
# MongoDB tap 추가
brew tap mongodb/brew

# MongoDB Community Edition 설치
brew install mongodb-community

# MongoDB 서비스 시작
brew services start mongodb/brew/mongodb-community

# 설치 확인
mongosh --eval "db.version()"
```

#### Linux (Ubuntu/Debian)

```bash
# MongoDB 공식 GPG 키 추가
wget -qO - https://www.mongodb.org/static/pgp/server-8.2.asc | sudo apt-key add -

# MongoDB 저장소 추가
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/8.2 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-8.2.list

# 패키지 업데이트 및 설치
sudo apt-get update
sudo apt-get install -y mongodb-org

# MongoDB 서비스 시작
sudo systemctl start mongod
sudo systemctl enable mongod

# 상태 확인
sudo systemctl status mongod
```

#### Windows

1. [MongoDB 다운로드 페이지](https://www.mongodb.com/try/download/community)에서 Windows 설치 프로그램 다운로드
2. 설치 프로그램 실행 및 기본 설정으로 설치
3. MongoDB Compass (GUI 도구)도 함께 설치 권장
4. 서비스가 자동으로 시작됨

</br>

### 3. 애플리케이션 설정

#### application.properties 파일 생성

`src/main/resources/application.properties` 파일을 생성하고 다음 내용을 추가합니다:

```properties
# Application Name
spring.application.name=BitFinanace

# MongoDB Configuration (TimeSeries 데이터 저장)
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=bitfinance

# Binance WebSocket URL
binance.websocket.url=wss://stream.binance.com:9443/ws/btcusdt@trade

# PostgreSQL Configuration (선택사항 - 향후 사용)
# spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
# spring.datasource.username=myuser
# spring.datasource.password=secret

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Actuator Endpoints
management.endpoints.web.exposure.include=health,metrics,info

# Swagger UI Configuration
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs

# Logging
logging.level.com.finpuff.bitfinanace=INFO
logging.level.org.springframework.web=INFO
```

#### 환경별 설정 (선택사항)

**개발 환경** - `application-dev.properties`:
```properties
logging.level.com.finpuff.bitfinanace=DEBUG
spring.jpa.show-sql=true
```

**운영 환경** - `application-prod.properties`:
```properties
logging.level.com.finpuff.bitfinanace=WARN
spring.jpa.show-sql=false
```

</br>

### 4. 애플리케이션 빌드

```bash
# Gradle Wrapper를 사용한 빌드
./gradlew clean build

# Windows의 경우
# gradlew.bat clean build
```

**빌드 시 주의사항:**
- 첫 빌드는 의존성 다운로드로 시간이 소요될 수 있습니다
- 테스트를 제외하고 빌드: `./gradlew clean build -x test`

</br>

### 5. 애플리케이션 실행

```bash
# Gradle Wrapper를 사용한 실행
./gradlew bootRun

# 또는 빌드된 JAR 파일 직접 실행
java -jar build/libs/BitFinanace-0.0.1-SNAPSHOT.jar
```

**실행 확인:**
```bash
# 애플리케이션 상태 확인
curl http://localhost:8080/actuator/health

# 예상 응답: {"status":"UP"}
```

</br>

### 6. 애플리케이션 접속

애플리케이션이 정상적으로 시작되면 다음 URL로 접속할 수 있습니다:

| 서비스 | URL | 설명 |
|--------|-----|------|
| 애플리케이션 | http://localhost:8080 | 메인 애플리케이션 |
| Swagger UI | http://localhost:8080/swagger-ui.html | API 문서 및 테스트 |
| OpenAPI Docs | http://localhost:8080/v3/api-docs | OpenAPI 3.0 스펙 |
| Health Check | http://localhost:8080/actuator/health | 헬스 체크 |
| Metrics | http://localhost:8080/actuator/metrics | 시스템 메트릭 |

</br>

## 🐳 Docker를 사용한 실행 (선택사항)

### Docker Compose로 MongoDB 실행

`docker-compose.yml` 파일이 있는 경우:

```bash
# MongoDB 컨테이너 시작
docker-compose up -d mongodb

# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f mongodb

# 컨테이너 중지
docker-compose down
```

</br>

## 🧪 설치 확인

### 1. MongoDB 연결 확인

```bash
# MongoDB Shell 접속
mongosh

# 데이터베이스 목록 확인
show dbs

# BitFinance 데이터베이스 선택
use bitfinance

# 컬렉션 확인
show collections
```

### 2. API 동작 확인

```bash
# 최신 비트코인 시세 조회
curl http://localhost:8080/api/v1/bitcoin/prices/latest

# 최근 24시간 시세 조회
curl http://localhost:8080/api/v1/bitcoin/prices/day
```

### 3. Swagger UI 확인

브라우저에서 http://localhost:8080/swagger-ui.html 접속 후:
1. "Bitcoin Price" 컨트롤러 확인
2. "GET /api/v1/bitcoin/prices/latest" API 테스트
3. "Try it out" 버튼 클릭 → "Execute"

</br>

## 🔧 문제 해결

### MongoDB 연결 실패

**증상:**
```
com.mongodb.MongoSocketOpenException: Exception opening socket
```

**해결 방법:**
```bash
# MongoDB 서비스 상태 확인
brew services list  # macOS
sudo systemctl status mongod  # Linux

# MongoDB 재시작
brew services restart mongodb/brew/mongodb-community  # macOS
sudo systemctl restart mongod  # Linux
```

### Port 8080 이미 사용 중

**증상:**
```
Port 8080 is already in use
```

**해결 방법:**

1. 다른 포트 사용:
   ```properties
   # application.properties에 추가
   server.port=8081
   ```

2. 기존 프로세스 종료 (macOS/Linux):
   ```bash
   lsof -ti:8080 | xargs kill -9
   ```

### Java 버전 불일치

**증상:**
```
Unsupported class file major version
```

**해결 방법:**
```bash
# Java 버전 확인
java -version

# Java 25 설치 확인
/usr/libexec/java_home -V  # macOS

# 환경 변수 설정 (필요시)
export JAVA_HOME=$(/usr/libexec/java_home -v 25)
```

### Gradle 빌드 실패

**증상:**
```
Could not resolve dependencies
```

**해결 방법:**
```bash
# Gradle 캐시 정리
./gradlew clean --refresh-dependencies

# Gradle Daemon 재시작
./gradlew --stop
./gradlew clean build
```

</br>

## 📝 개발 모드 실행

### Hot Reload 활성화

Spring Boot DevTools가 활성화되어 있으면 코드 변경 시 자동으로 재시작됩니다.

```properties
# application.properties
spring.devtools.restart.enabled=true
spring.devtools.livereload.enabled=true
```

### 디버그 모드 실행

```bash
# IntelliJ IDEA에서 Debug 버튼 클릭
# 또는
./gradlew bootRun --debug-jvm
```

</br>

## 🔄 업데이트 및 재배포

### 코드 업데이트 후

```bash
# 최신 코드 가져오기
git pull origin main

# 의존성 업데이트 및 재빌드
./gradlew clean build --refresh-dependencies

# 애플리케이션 재시작
./gradlew bootRun
```

### MongoDB 데이터 초기화 (필요시)

```bash
# MongoDB Shell 접속
mongosh

# 데이터베이스 삭제
use bitfinance
db.dropDatabase()

# 애플리케이션 재시작 (자동으로 컬렉션 생성됨)
```

</br>

## 📚 추가 리소스

- [Spring Boot 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MongoDB 설치 가이드](https://docs.mongodb.com/manual/installation/)
- [Gradle 사용법](https://docs.gradle.org/current/userguide/userguide.html)
- [프로젝트 의존성 상세](./DEPENDENCIES.md)

</br>

## 💡 팁

### 개발 생산성 향상

1. **IntelliJ IDEA 플러그인 추천:**
   - Lombok Plugin
   - Spring Boot Assistant
   - MongoDB Plugin

2. **MongoDB GUI 도구:**
   - MongoDB Compass (공식)
   - Robo 3T
   - Studio 3T

3. **API 테스트 도구:**
   - Swagger UI (내장)
   - Postman
   - cURL

### 성능 최적화

```properties
# application.properties에 추가
# JVM 힙 메모리 설정
JAVA_OPTS=-Xms512m -Xmx1024m

# MongoDB 연결 풀 설정
spring.data.mongodb.option.max-pool-size=50
spring.data.mongodb.option.min-pool-size=10
```

</br>

---

설치 중 문제가 발생하면 [이슈 트래커](https://github.com/yourusername/BitFinanace/issues)에 문의해주세요.

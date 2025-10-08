# 💰 BitFinance - Real-time Cryptocurrency Asset Management Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-8.2-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> 실시간 암호화폐 및 디지털 자산 정보를 수집, 저장, 분석하여 사용자에게 제공하는 엔터프라이즈급 백엔드 시스템

<br/>

## 📋 프로젝트 개요

BitFinance는 비트코인을 비롯한 다양한 암호화폐 자산의 실시간 시세 정보를 Binance WebSocket을 통해 수집하고, MongoDB TimeSeries Collection에 저장하여 사용자에게 RESTful API로 제공하는 백엔드 플랫폼입니다. Spring Boot 기반의 현대적인 아키텍처와 WebSocket을 활용한 실시간 데이터 스트리밍을 지원합니다.

### 🎯 주요 기능

- **실시간 비트코인 시세 수집**: Binance WebSocket을 통한 실시간 BTC/USDT 거래 데이터 수집
- **TimeSeries 데이터 저장**: MongoDB TimeSeries Collection을 활용한 효율적인 시계열 데이터 관리
- **RESTful API 제공**: 일/주/월 단위 시세 조회 및 최신 가격 조회 API
- **API 문서화**: Swagger/OpenAPI를 통한 인터랙티브 API 문서 제공
- **실시간 알림**: WebSocket 기반의 실시간 시세 변동 알림 시스템
- **보안 인증**: OAuth2 + Spring Security 기반의 안전한 사용자 인증/인가
- **시스템 모니터링**: Spring Boot Actuator를 통한 애플리케이션 헬스 체크 및 메트릭 수집

<br/>
  

## 🛠️ Git Commit Convention

이 프로젝트는 일관성 있는 커밋 히스토리 관리를 위해 Conventional Commits 규칙을 따릅니다.

```bash
# 예시
feat(api): 비트코인 시세 조회 API 추가
fix(websocket): 연결 끊김 오류 수정
docs(readme): 설치 가이드 업데이트
```

자세한 커밋 규칙은 **[COMMIT_CONVENTION.md](./COMMIT_CONVENTION.md)** 문서를 참고해주세요.

<br/>

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

<br/>

## 🏗️ 기술 스택

### Backend Framework
- **Spring Boot 3.5.7-SNAPSHOT** - 최신 Spring Boot 프레임워크
- **Java 25** - 최신 Java 버전 사용

### 주요 기술
- **Spring Data MongoDB** - TimeSeries Collection 지원
- **Spring Data JPA/JDBC** - 관계형 데이터베이스 연동
- **Spring Security + OAuth2** - 인증/인가
- **Spring WebSocket** - 실시간 통신
- **Springdoc OpenAPI** - API 문서화 (Swagger UI)
- **Java-WebSocket** - Binance 실시간 데이터 수집

### Database
- **MongoDB 8.2** - 시계열 데이터 저장
- **PostgreSQL** - 사용자/트랜잭션 데이터

> 📦 **상세 의존성 정보**: [DEPENDENCIES.md](./DEPENDENCIES.md) 참고

<br/>

## 🚀 빠른 시작

### 사전 요구사항
- Java 25 이상
- MongoDB 8.2 이상
- Gradle 8.x

### 설치 및 실행

```bash
# 1. 저장소 클론
git clone https://github.com/yourusername/BitFinanace.git
cd BitFinanace

# 2. MongoDB 시작 (macOS)
brew services start mongodb/brew/mongodb-community

# 3. 애플리케이션 실행
./gradlew bootRun

# 4. Swagger UI 접속
# http://localhost:8080/swagger-ui.html
```

> 📖 **상세 설치 가이드**: [INSTALLATION.md](./INSTALLATION.md) 참고

<br/>

## 📊 시스템 아키텍처

```text
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

<br/>

## 📖 API 문서

**Swagger UI**를 통해 모든 API를 확인하고 테스트할 수 있습니다:

- **Swagger UI**: <http://localhost:8080/swagger-ui.html>
- **OpenAPI 문서**: <http://localhost:8080/v3/api-docs>

<br/>

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests BitFinanaceApplicationTests

# 테스트 커버리지 리포트 생성
./gradlew jacocoTestReport
```

<br/>


## 📝 개발 로드맵

- [x] 프로젝트 초기 구조 설정
- [x] Spring Boot 기반 아키텍처 구축
- [x] Binance WebSocket 연동 (실시간 BTC 시세)
- [x] MongoDB TimeSeries Collection 구축
- [x] RESTful API 엔드포인트 개발 (일/주/월 시세 조회)
- [x] Swagger/OpenAPI 문서화
- [ ] 다양한 암호화폐 지원 (ETH, XRP 등)
- [ ] WebSocket 실시간 스트리밍 클라이언트 구현
- [ ] OAuth2 소셜 로그인 구현
- [ ] 데이터 분석 및 차트 API
- [ ] 알림 시스템 (이메일, 푸시)
- [ ] 관리자 대시보드
- [ ] Docker 배포 최적화
- [ ] Kubernetes 오케스트레이션
- [ ] CI/CD 파이프라인 구축


<br/>

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (커밋 컨벤션을 따라주세요)
   - **[커밋 컨벤션 가이드 보기](./COMMIT_CONVENTION.md)**
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<br/>

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

<br/>

## 👤 개발자

**Finpuff Team**

- GitHub: [@movejae](https://github.com/movejae)
- Email: <contact@finpuff.com>

## 🙏 감사의 말

- [Spring Framework](https://spring.io/)
- [CoinGecko API](https://www.coingecko.com/en/api)
- [Binance API](https://binance-docs.github.io/apidocs/)

---

⭐️ 이 프로젝트가 도움이 되셨다면 Star를 눌러주세요!

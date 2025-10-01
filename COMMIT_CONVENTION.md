# 📝 Git Commit Convention

이 문서는 BitFinance 프로젝트의 Git 커밋 메시지 작성 규칙을 정의합니다.

## 🎯 커밋 메시지 구조

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 기본 형식
```
type(scope): subject
```

**예시:**
```
feat(api): 비트코인 실시간 시세 조회 API 추가
```

## 📌 Type (필수)

커밋의 유형을 나타냅니다. 다음 중 하나를 사용해야 합니다:

| Type | 설명 | 예시 |
|------|------|------|
| `feat` | 새로운 기능 추가 | `feat(auth): OAuth2 구글 로그인 구현` |
| `fix` | 버그 수정 | `fix(websocket): 연결 끊김 오류 수정` |
| `docs` | 문서 수정 | `docs(readme): 설치 가이드 업데이트` |
| `style` | 코드 포맷팅, 세미콜론 누락 등 (동작 변경 없음) | `style(controller): 코드 포맷팅 적용` |
| `refactor` | 코드 리팩토링 (기능 변경 없음) | `refactor(service): 데이터 수집 로직 개선` |
| `test` | 테스트 코드 추가/수정 | `test(api): 자산 조회 API 테스트 추가` |
| `chore` | 빌드 설정, 패키지 매니저 수정 등 | `chore(deps): Spring Boot 3.6.0 업그레이드` |
| `perf` | 성능 개선 | `perf(db): 인덱스 추가로 쿼리 성능 개선` |
| `ci` | CI/CD 설정 변경 | `ci(github): GitHub Actions 워크플로우 추가` |
| `build` | 빌드 시스템 수정 | `build(gradle): 멀티 모듈 설정` |
| `revert` | 이전 커밋 되돌리기 | `revert: feat(api): 비트코인 API 추가` |

## 🎯 Scope (선택)

변경된 부분의 범위를 나타냅니다. 프로젝트 구조에 따라 자유롭게 정의할 수 있습니다.

### BitFinance 프로젝트 Scope 예시

| Scope | 설명 |
|-------|------|
| `api` | REST API 엔드포인트 |
| `websocket` | WebSocket 실시간 통신 |
| `auth` | 인증/인가 관련 |
| `db` | 데이터베이스 스키마, 마이그레이션 |
| `entity` | JPA 엔티티 |
| `service` | 비즈니스 로직 |
| `repository` | 데이터 액세스 레이어 |
| `scheduler` | 스케줄러, 배치 작업 |
| `config` | 설정 파일 |
| `security` | 보안 설정 |
| `dto` | DTO, Request/Response 객체 |
| `exception` | 예외 처리 |
| `util` | 유틸리티 클래스 |
| `validator` | 유효성 검증 |

## ✍️ Subject (필수)

커밋의 간단한 설명입니다.

### 규칙
- 50자 이내로 작성
- 명령형 현재 시제 사용 (추가, 수정, 제거)
- 첫 글자는 소문자
- 마침표(.) 사용하지 않음
- 한글 또는 영어 사용 가능

### 좋은 예시
```
feat(api): 이더리움 시세 조회 API 추가
fix(websocket): 세션 타임아웃 오류 수정
docs(readme): 환경 변수 설정 가이드 추가
refactor(service): 중복 코드 제거 및 메서드 분리
```

### 나쁜 예시
```
feat: 기능 추가함.  // 마침표 사용, 구체적이지 않음
Fix: Bug fix  // 첫 글자 대문자
api 추가  // type 누락
```

## 📄 Body (선택)

커밋의 상세한 설명이 필요한 경우 작성합니다.

### 규칙
- Subject와 한 줄 띄우고 작성
- 무엇을, 왜 변경했는지 설명
- 72자마다 줄바꿈
- 어떻게 변경했는지보다는 왜 변경했는지에 집중

### 예시
```
feat(scheduler): 암호화폐 시세 자동 수집 스케줄러 구현

외부 API를 통해 1분마다 비트코인, 이더리움 시세를 자동으로 수집하여
데이터베이스에 저장하는 스케줄러를 구현했습니다.

- CoinGecko API 연동
- 1분 간격 cron 표현식 적용
- API 호출 실패 시 재시도 로직 추가
- 수집된 데이터 PostgreSQL 저장
```

## 🔗 Footer (선택)

이슈 트래커 ID, Breaking Change 등을 명시합니다.

### 규칙
- `Fixes #이슈번호`: 이슈 해결
- `Closes #이슈번호`: 이슈 종료
- `Refs #이슈번호`: 이슈 참조
- `BREAKING CHANGE:`: 하위 호환성이 깨지는 변경사항

### 예시
```
fix(auth): JWT 토큰 만료 시간 검증 로직 수정

토큰 만료 시간이 정확히 검증되지 않는 버그를 수정했습니다.

Fixes #123
```

```
feat(api): REST API v2 엔드포인트 추가

BREAKING CHANGE: /api/assets 엔드포인트가 /api/v2/assets로 변경되었습니다.
기존 v1 API는 2024년 12월 31일까지 지원됩니다.
```

## 🌟 실전 예시

### 1. 간단한 기능 추가
```
feat(api): 비트코인 24시간 거래량 조회 API 추가
```

### 2. 버그 수정
```
fix(websocket): 클라이언트 연결 해제 시 메모리 누수 수정

WebSocket 세션 종료 시 리스너가 제거되지 않아
메모리 누수가 발생하는 문제를 수정했습니다.

Fixes #45
```

### 3. 복잡한 기능 추가
```
feat(scheduler): 다중 거래소 시세 데이터 수집 시스템 구현

Binance, Upbit, Bithumb 3개 거래소의 실시간 시세를
병렬로 수집하는 시스템을 구현했습니다.

- 각 거래소별 API 클라이언트 추가
- ExecutorService를 이용한 병렬 처리
- 거래소별 Rate Limit 관리
- 에러 발생 시 알림 메일 발송

Closes #78
```

### 4. 리팩토링
```
refactor(service): 자산 데이터 검증 로직을 별도 클래스로 분리

AssetService의 비대해진 검증 로직을 AssetValidator로 분리하여
단일 책임 원칙을 준수하도록 개선했습니다.
```

### 5. 문서 작성
```
docs: 커밋 컨벤션 가이드 작성

팀 협업을 위한 Git 커밋 메시지 작성 규칙을 문서화했습니다.
```

### 6. 의존성 업데이트
```
chore(deps): Spring Boot 3.6.0으로 업그레이드

보안 패치 및 성능 개선이 포함된 Spring Boot 최신 버전으로
업그레이드했습니다.
```

### 7. 성능 개선
```
perf(db): Asset 테이블에 복합 인덱스 추가

symbol과 timestamp 컬럼에 복합 인덱스를 추가하여
시세 조회 쿼리 성능을 약 10배 개선했습니다.

Before: 평균 500ms
After: 평균 50ms
```

### 8. 테스트 추가
```
test(api): 자산 CRUD API 통합 테스트 추가

RestAssured를 이용한 REST API 통합 테스트를 추가하여
엔드투엔드 시나리오를 검증합니다.
```

## 🚫 피해야 할 커밋 메시지

```
// ❌ 나쁜 예시
커밋
asdf
작업 완료
수정함
버그 수정
업데이트
123
fix
WIP
```

```
// ✅ 좋은 예시
feat(api): 리플 시세 조회 기능 추가
fix(scheduler): 중복 데이터 저장 방지
refactor(service): 코드 가독성 개선
docs(api): Swagger 문서 업데이트
```

## 💡 커밋 팁

### 1. 의미 있는 단위로 커밋하기
하나의 커밋에는 하나의 의미 있는 변경사항만 포함합니다.

```bash
# ❌ 나쁜 예: 여러 기능을 한 번에 커밋
git add .
git commit -m "feat: API 추가, 버그 수정, 문서 업데이트"

# ✅ 좋은 예: 기능별로 나눠서 커밋
git add src/api/bitcoin.controller.ts
git commit -m "feat(api): 비트코인 시세 조회 API 추가"

git add src/service/asset.service.ts
git commit -m "fix(service): null 체크 로직 추가"

git add README.md
git commit -m "docs(readme): API 사용 예시 추가"
```

### 2. 커밋 전 검토하기
```bash
# 변경 사항 확인
git status
git diff

# 스테이징된 파일 확인
git diff --staged
```

### 3. 커밋 메시지 수정하기
```bash
# 마지막 커밋 메시지 수정
git commit --amend

# 이전 커밋 수정 (interactive rebase)
git rebase -i HEAD~3
```

## 🔧 IntelliJ IDEA 커밋 템플릿 설정

IntelliJ IDEA에서 커밋 템플릿을 사용하려면:

1. **Settings** → **Version Control** → **Commit** 이동
2. **Commit message** 템플릿 설정:

```
<type>(<scope>): <subject>

# Body (선택사항)


# Footer (선택사항)


# Type: feat, fix, docs, style, refactor, test, chore, perf, ci, build, revert
# Scope: api, websocket, auth, db, entity, service, etc.
# Subject: 50자 이내, 명령형 현재 시제, 소문자 시작, 마침표 없음
```

## 📚 참고 자료

- [Conventional Commits](https://www.conventionalcommits.org/)
- [Angular Commit Guidelines](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#commit)
- [Git Commit Message Best Practices](https://chris.beams.io/posts/git-commit/)

---

**이 가이드를 따라 일관성 있는 커밋 히스토리를 만들어주세요!** 🚀

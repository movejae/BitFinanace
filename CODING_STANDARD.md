# 🎨 BitFinance Coding Standard

이 문서는 BitFinance 프로젝트의 코딩 스타일과 설계 원칙을 정의합니다.

## 📐 설계 원칙

### 1. 도메인 주도 설계 (Domain-Driven Design)

비즈니스 로직을 도메인 모델 중심으로 구성하고, 도메인 객체를 통해 응집도 높은 코드를 작성합니다.

### 2. 안전한 객체 생성 (Safe Object Creation)

**중요한 필드(ID, 필수값)가 누락된 상태로 비즈니스 로직을 실행하면 런타임 에러가 발생합니다.**
객체 생성 시 필수 필드를 반드시 초기화하고, 생성 후 상태 변경은 허용합니다.

### 3. 비즈니스 로직은 도메인 객체에

비교, 검증, 계산 로직은 도메인 엔티티 내부에 위치시켜 중복을 방지하고 응집도를 높입니다.

### 4. 명시적 의도 (Explicit Intent)

코드를 읽는 사람이 메서드명과 타입만으로 의도를 파악할 수 있도록 작성합니다.

---

## 🏗️ 도메인 설계

### ✅ 생성자와 빌더 패턴

**원칙: 필수 필드(ID, 필수값)가 누락된 상태로 비즈니스 로직을 타면 안됩니다!**

Builder 패턴으로 객체를 생성하되, 생성 후 상태 변경은 허용합니다. 다만 **중요한 필드가 null인 상태로 계산 로직을 실행하면 NullPointerException이 발생**하므로 필수 필드는 반드시 초기화해야 합니다.

#### ❌ 나쁜 예시 - 필수 필드 누락

```java
@Entity
@Getter
@Setter
public class Asset {
    @Id
    private Long id;
    private String symbol;
    private BigDecimal price;
    private LocalDateTime timestamp;

    // ❌ 필수 필드를 체크하지 않음
    public BigDecimal calculateValue(BigDecimal quantity) {
        return this.price.multiply(quantity);  // price가 null이면 NPE 발생!
    }
}

// 사용 예시
Asset asset = new Asset();
asset.setSymbol("BTC");  // ❌ price가 설정 안된 상태
BigDecimal value = asset.calculateValue(new BigDecimal("10"));  // 💥 NPE 발생!
```

#### ✅ 좋은 예시 - Builder로 필수 필드 보장

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // Builder에서만 사용
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal price;  // 필수 필드!

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // ✅ 필수 필드가 보장된 상태에서 계산 로직 실행
    public BigDecimal calculateValue(BigDecimal quantity) {
        Objects.requireNonNull(price, "Price must not be null");
        return this.price.multiply(quantity);
    }

    // 도메인 로직
    public boolean isPriceAbove(BigDecimal threshold) {
        return this.price.compareTo(threshold) > 0;
    }

    // ⚠️ 상태 변경은 허용
    public void updatePrice(BigDecimal newPrice) {
        Objects.requireNonNull(newPrice, "New price must not be null");
        this.price = newPrice;
        this.timestamp = LocalDateTime.now();
    }
}

// 사용 예시
Asset asset = Asset.builder()
        .symbol("BTC")
        .price(new BigDecimal("50000"))  // ✅ 필수 필드 초기화
        .timestamp(LocalDateTime.now())
        .build();

// 생성 후 상태 변경도 가능
asset.updatePrice(new BigDecimal("51000"));  // ✅ 허용
```

---

### ✅ 비교/검증 로직은 도메인 객체에 위치

**원칙: 비교 로직을 여러 곳에 중복으로 작성하지 말고, 도메인 객체 내부에 메서드로 만드세요!**

#### ❌ 나쁜 예시 - 비교 로직 중복

```java
// ❌ Service 곳곳에 나이 비교 로직이 중복됨
@Service
public class UserService {

    public boolean canDrink(User user) {
        return user.getAge() >= 19;  // ❌ 중복 로직 1
    }

    public boolean canVote(User user) {
        return user.getAge() >= 19;  // ❌ 중복 로직 2
    }

    public List<User> filterAdults(List<User> users) {
        return users.stream()
                .filter(user -> user.getAge() >= 19)  // ❌ 중복 로직 3
                .collect(Collectors.toList());
    }
}

@Service
public class PromotionService {

    public boolean isEligible(User user) {
        return user.getAge() >= 19;  // ❌ 중복 로직 4
    }
}
```

#### ✅ 좋은 예시 - 도메인 객체에 비교 로직 캡슐화

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private LocalDate birthDate;

    // ✅ 비교 로직을 도메인 객체 안에!
    public boolean isAdult() {
        return this.age >= 19;
    }

    public boolean isMinor() {
        return this.age < 19;
    }

    public boolean isOlderThan(int ageThreshold) {
        return this.age > ageThreshold;
    }

    public boolean isBirthday() {
        LocalDate today = LocalDate.now();
        return this.birthDate.getMonth() == today.getMonth()
               && this.birthDate.getDayOfMonth() == today.getDayOfMonth();
    }
}

// ✅ Service에서는 도메인 메서드 호출만
@Service
public class UserService {

    public boolean canDrink(User user) {
        return user.isAdult();  // ✅ 명확하고 중복 없음
    }

    public boolean canVote(User user) {
        return user.isAdult();  // ✅ 명확하고 중복 없음
    }

    public List<User> filterAdults(List<User> users) {
        return users.stream()
                .filter(User::isAdult)  // ✅ 메서드 참조
                .collect(Collectors.toList());
    }
}

@Service
public class PromotionService {

    public boolean isEligible(User user) {
        return user.isAdult();  // ✅ 명확하고 중복 없음
    }
}
```

#### 🎯 도메인 로직 배치 가이드

```java
@Entity
public class Asset {

    // ✅ 비교 로직
    public boolean isPriceAbove(BigDecimal threshold) {
        return this.price.compareTo(threshold) > 0;
    }

    public boolean isPriceBelow(BigDecimal threshold) {
        return this.price.compareTo(threshold) < 0;
    }

    public boolean isPriceInRange(BigDecimal min, BigDecimal max) {
        return this.price.compareTo(min) >= 0 && this.price.compareTo(max) <= 0;
    }

    // ✅ 계산 로직
    public BigDecimal calculateValue(BigDecimal quantity) {
        return this.price.multiply(quantity);
    }

    public BigDecimal calculateChangeRate(BigDecimal previousPrice) {
        if (previousPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return this.price.subtract(previousPrice)
                .divide(previousPrice, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    // ✅ 검증 로직
    public boolean isValidForTrading() {
        return this.price != null
               && this.price.compareTo(BigDecimal.ZERO) > 0
               && this.timestamp != null;
    }

    // ✅ 상태 확인 로직
    public boolean isRecentlyUpdated() {
        Duration duration = Duration.between(this.timestamp, LocalDateTime.now());
        return duration.toMinutes() < 5;
    }
}
```

---

## 📦 메서드 파라미터 설계

### ✅ 파라미터가 많으면 객체로 묶기

**원칙: String 파라미터가 2개 이상이면 객체로 묶습니다.**

#### ❌ 나쁜 예시 - 원시 타입 파라미터 남발

```java
@Service
public class AssetService {

    // ❌ 파라미터가 너무 많고, 의미가 불명확
    public Asset createAsset(String symbol, String name, String category,
                            String description, String imageUrl,
                            BigDecimal price, LocalDateTime timestamp) {
        // ...
    }

    // ❌ String 파라미터가 순서를 바꿔도 컴파일 에러가 안남
    public List<Asset> searchAssets(String symbol, String category, String sortBy) {
        // symbol과 category를 실수로 바꿔도 컴파일은 통과
    }
}

// 사용 예시
assetService.createAsset(
    "BTC", "Bitcoin", "Crypto",
    "Digital currency", "https://...",
    new BigDecimal("50000"), LocalDateTime.now()
);  // ❌ 가독성 떨어지고 실수하기 쉬움
```

#### ✅ 좋은 예시 - DTO/Command 객체 사용

```java
// 요청 객체 정의
@Getter
@Builder
public class CreateAssetCommand {
    private final String symbol;
    private final String name;
    private final AssetCategory category;
    private final String description;
    private final String imageUrl;
    private final BigDecimal initialPrice;

    public Asset toEntity() {
        return Asset.builder()
                .symbol(this.symbol)
                .name(this.name)
                .category(this.category)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .price(this.initialPrice)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

@Getter
@Builder
public class AssetSearchQuery {
    private final String symbol;
    private final AssetCategory category;
    private final SortOption sortBy;
    private final int page;
    private final int size;
}

@Service
public class AssetService {

    // ✅ 명확한 의도, 타입 안정성
    public Asset createAsset(CreateAssetCommand command) {
        Asset asset = command.toEntity();
        return assetRepository.save(asset);
    }

    // ✅ 검색 조건이 명확하고 확장 가능
    public Page<Asset> searchAssets(AssetSearchQuery query) {
        // ...
    }
}

// 사용 예시
CreateAssetCommand command = CreateAssetCommand.builder()
        .symbol("BTC")
        .name("Bitcoin")
        .category(AssetCategory.CRYPTOCURRENCY)
        .description("Digital currency")
        .imageUrl("https://...")
        .initialPrice(new BigDecimal("50000"))
        .build();

Asset asset = assetService.createAsset(command);  // ✅ 명확하고 안전
```

---

## 🎨 Enum 적극 활용

### ✅ 문자열 상수 대신 Enum 사용

**원칙: 정해진 값들의 집합은 Enum으로 표현하여 타입 안정성을 확보합니다.**

#### ❌ 나쁜 예시 - 문자열 상수 사용

```java
// ❌ 문자열 상수로 관리
public class AssetService {

    public static final String CATEGORY_CRYPTO = "CRYPTO";
    public static final String CATEGORY_STOCK = "STOCK";
    public static final String CATEGORY_COMMODITY = "COMMODITY";

    public List<Asset> findByCategory(String category) {
        // ❌ 오타 가능, 컴파일 시점에 잡을 수 없음
        if (category.equals("CRYPT0")) {  // 오타!
            // ...
        }
    }
}

// 사용 예시
assetService.findByCategory("CRYPTO");  // ❌ 오타 위험
assetService.findByCategory("crypto");  // ❌ 대소문자 실수
assetService.findByCategory("INVALID");  // ❌ 런타임에야 발견
```

#### ✅ 좋은 예시 - Enum 활용

```java
// ✅ Enum으로 정의
public enum AssetCategory {
    CRYPTOCURRENCY("암호화폐", "디지털 자산"),
    STOCK("주식", "기업 지분"),
    COMMODITY("원자재", "금, 은, 원유 등"),
    FOREX("외환", "통화 거래");

    private final String koreanName;
    private final String description;

    AssetCategory(String koreanName, String description) {
        this.koreanName = koreanName;
        this.description = description;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public String getDescription() {
        return description;
    }

    // ✅ Enum에 비즈니스 로직 추가 가능
    public boolean isDigitalAsset() {
        return this == CRYPTOCURRENCY;
    }

    public boolean requiresRealTimeData() {
        return this == CRYPTOCURRENCY || this == STOCK;
    }
}

// ✅ Enum을 파라미터로 받기
@Service
public class AssetService {

    public List<Asset> findByCategory(AssetCategory category) {
        return assetRepository.findByCategory(category);  // ✅ 타입 안전
    }

    public boolean isRealTimeTracking(AssetCategory category) {
        return category.requiresRealTimeData();  // ✅ Enum 메서드 활용
    }
}

// 사용 예시
assetService.findByCategory(AssetCategory.CRYPTOCURRENCY);  // ✅ 타입 안전
// assetService.findByCategory("CRYPTO");  // 컴파일 에러!
```

#### 🎯 Enum 활용 예시

```java
// 거래 유형
public enum TradeType {
    BUY("매수", "+"),
    SELL("매도", "-");

    private final String koreanName;
    private final String sign;

    TradeType(String koreanName, String sign) {
        this.koreanName = koreanName;
        this.sign = sign;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public String getSign() {
        return sign;
    }

    public boolean isBuy() {
        return this == BUY;
    }
}

// 정렬 옵션
public enum SortOption {
    PRICE_ASC("가격 낮은순", "price", Sort.Direction.ASC),
    PRICE_DESC("가격 높은순", "price", Sort.Direction.DESC),
    NAME_ASC("이름순", "name", Sort.Direction.ASC),
    VOLUME_DESC("거래량 높은순", "volume", Sort.Direction.DESC);

    private final String displayName;
    private final String fieldName;
    private final Sort.Direction direction;

    SortOption(String displayName, String fieldName, Sort.Direction direction) {
        this.displayName = displayName;
        this.fieldName = fieldName;
        this.direction = direction;
    }

    public Sort toSort() {
        return Sort.by(direction, fieldName);
    }
}

// 알림 타입
public enum NotificationType {
    PRICE_ALERT("가격 알림", true),
    TRADE_COMPLETE("거래 완료", true),
    SYSTEM_NOTICE("시스템 공지", false),
    PROMOTION("프로모션", false);

    private final String displayName;
    private final boolean isImportant;

    NotificationType(String displayName, boolean isImportant) {
        this.displayName = displayName;
        this.isImportant = isImportant;
    }

    public boolean shouldSendPush() {
        return isImportant;
    }
}
```

---

## 🔧 메서드 분리 원칙

### ✅ 재사용성 기준으로 메서드 분리

**원칙: 1회성 로직은 굳이 분리하지 않고, 여러 곳에서 사용될 로직만 메서드로 추출합니다.**

#### ❌ 나쁜 예시 - 과도한 메서드 분리

```java
@Service
public class AssetPriceService {

    public void updateAssetPrice(Long assetId, BigDecimal newPrice) {
        // ❌ 한 곳에서만 쓰는데 굳이 분리
        Asset asset = findAssetById(assetId);
        validatePrice(newPrice);
        BigDecimal oldPrice = getPreviousPrice(asset);
        logPriceChange(asset, oldPrice, newPrice);
        updatePrice(asset, newPrice);
        sendNotification(asset, newPrice);
    }

    // ❌ 여기서만 쓰이는 메서드들
    private Asset findAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException();
        }
    }

    private BigDecimal getPreviousPrice(Asset asset) {
        return asset.getPrice();
    }

    private void logPriceChange(Asset asset, BigDecimal old, BigDecimal newPrice) {
        log.info("Price changed: {} {} -> {}", asset.getSymbol(), old, newPrice);
    }

    private void updatePrice(Asset asset, BigDecimal price) {
        asset.setPrice(price);
        assetRepository.save(asset);
    }

    private void sendNotification(Asset asset, BigDecimal price) {
        notificationService.send("Price updated: " + asset.getSymbol());
    }
}
```

#### ✅ 좋은 예시 - 재사용 가능한 로직만 분리

```java
@Service
@RequiredArgsConstructor
public class AssetPriceService {

    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final NotificationService notificationService;

    @Transactional
    public void updateAssetPrice(Long assetId, BigDecimal newPrice) {
        // 1회성 로직은 한 곳에 모아서 읽기 쉽게
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException(assetId));

        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Price must be positive: " + newPrice);
        }

        BigDecimal oldPrice = asset.getPrice();

        // 가격 변경 (도메인 로직은 엔티티에)
        asset.updatePrice(newPrice);
        assetRepository.save(asset);

        // 히스토리 저장 (재사용됨)
        savePriceHistory(asset, oldPrice, newPrice);

        // 알림 발송 (재사용됨)
        notifyPriceChange(asset, oldPrice, newPrice);

        log.info("Asset price updated: {} {} -> {}",
                asset.getSymbol(), oldPrice, newPrice);
    }

    // ✅ 다른 곳에서도 사용되는 히스토리 저장 로직
    private void savePriceHistory(Asset asset, BigDecimal oldPrice, BigDecimal newPrice) {
        PriceHistory history = PriceHistory.builder()
                .assetId(asset.getId())
                .symbol(asset.getSymbol())
                .previousPrice(oldPrice)
                .currentPrice(newPrice)
                .changeRate(calculateChangeRate(oldPrice, newPrice))
                .timestamp(LocalDateTime.now())
                .build();

        priceHistoryRepository.save(history);
    }

    // ✅ 변동률 계산은 여러 곳에서 사용됨
    private BigDecimal calculateChangeRate(BigDecimal oldPrice, BigDecimal newPrice) {
        if (oldPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return newPrice.subtract(oldPrice)
                .divide(oldPrice, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    // ✅ 알림 로직은 여러 상황에서 재사용됨
    private void notifyPriceChange(Asset asset, BigDecimal oldPrice, BigDecimal newPrice) {
        BigDecimal changeRate = calculateChangeRate(oldPrice, newPrice);

        // 5% 이상 변동 시에만 알림
        if (changeRate.abs().compareTo(new BigDecimal("5")) >= 0) {
            PriceChangeNotification notification = PriceChangeNotification.builder()
                    .symbol(asset.getSymbol())
                    .oldPrice(oldPrice)
                    .newPrice(newPrice)
                    .changeRate(changeRate)
                    .build();

            notificationService.sendPriceAlert(notification);
        }
    }

    // ✅ 배치에서도 사용되는 대량 업데이트
    @Transactional
    public void bulkUpdatePrices(List<AssetPriceUpdate> updates) {
        for (AssetPriceUpdate update : updates) {
            updateAssetPrice(update.getAssetId(), update.getNewPrice());
        }
    }
}
```

#### 🎯 메서드 분리 기준 정리

```java
// ❌ 분리하지 않아도 되는 경우
// - 한 곳에서만 사용되는 단순 로직
// - 비즈니스 흐름의 일부로 순차적으로 실행되는 코드
// - 메서드로 분리해도 가독성이 개선되지 않는 경우

// ✅ 반드시 분리해야 하는 경우
// - 2곳 이상에서 사용되는 로직
// - 복잡한 계산 로직 (변동률, 수수료 등)
// - 외부 API 호출
// - 도메인 규칙 검증
// - 알림, 로깅 같은 부가 기능
```

---

## 🌊 Stream API 적극 활용

### ✅ 컬렉션 처리는 Stream API 사용

**원칙: for문 대신 Stream API를 사용하여 선언적이고 읽기 쉬운 코드를 작성합니다.**

#### ❌ 나쁜 예시 - 전통적인 for문

```java
@Service
public class AssetService {

    // ❌ 전통적인 for문 사용
    public List<Asset> filterExpensiveAssets(List<Asset> assets, BigDecimal threshold) {
        List<Asset> result = new ArrayList<>();
        for (Asset asset : assets) {
            if (asset.getPrice().compareTo(threshold) > 0) {
                result.add(asset);
            }
        }
        return result;
    }

    // ❌ 중첩 for문
    public BigDecimal calculateTotalValue(List<Asset> assets) {
        BigDecimal total = BigDecimal.ZERO;
        for (Asset asset : assets) {
            total = total.add(asset.getPrice());
        }
        return total;
    }

    // ❌ 변환 로직
    public List<String> getSymbols(List<Asset> assets) {
        List<String> symbols = new ArrayList<>();
        for (Asset asset : assets) {
            symbols.add(asset.getSymbol());
        }
        return symbols;
    }
}
```

#### ✅ 좋은 예시 - Stream API 활용

```java
@Service
public class AssetService {

    // ✅ filter 사용
    public List<Asset> filterExpensiveAssets(List<Asset> assets, BigDecimal threshold) {
        return assets.stream()
                .filter(asset -> asset.isPriceAbove(threshold))  // 도메인 메서드 활용!
                .collect(Collectors.toList());
    }

    // ✅ reduce 사용
    public BigDecimal calculateTotalValue(List<Asset> assets) {
        return assets.stream()
                .map(Asset::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ map 사용
    public List<String> getSymbols(List<Asset> assets) {
        return assets.stream()
                .map(Asset::getSymbol)
                .collect(Collectors.toList());
    }

    // ✅ 복잡한 체이닝
    public Map<String, BigDecimal> getTopExpensiveAssets(List<Asset> assets, int limit) {
        return assets.stream()
                .sorted(Comparator.comparing(Asset::getPrice).reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Asset::getSymbol,
                        Asset::getPrice
                ));
    }

    // ✅ groupingBy 활용
    public Map<AssetCategory, List<Asset>> groupByCategory(List<Asset> assets) {
        return assets.stream()
                .collect(Collectors.groupingBy(Asset::getCategory));
    }

    // ✅ 통계 계산
    public BigDecimal calculateAveragePrice(List<Asset> assets) {
        return assets.stream()
                .map(Asset::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(assets.size()), RoundingMode.HALF_UP);
    }

    // ✅ 도메인 메서드와 Stream 조합
    public List<Asset> findRecentlyUpdatedExpensiveAssets(
            List<Asset> assets, BigDecimal threshold) {
        return assets.stream()
                .filter(Asset::isRecentlyUpdated)  // 도메인 메서드
                .filter(asset -> asset.isPriceAbove(threshold))  // 도메인 메서드
                .collect(Collectors.toList());
    }
}
```

---

## 🚫 람다 표현식 최소화

### ✅ 람다 대신 명시적 메서드 사용

**원칙: 복잡한 람다 표현식은 가독성을 해치므로, 별도 메서드로 추출합니다.**

#### ❌ 나쁜 예시 - 복잡한 람다

```java
@Service
public class AssetAnalysisService {

    // ❌ 람다가 너무 복잡해서 읽기 어려움
    public List<Asset> analyzeAssets(List<Asset> assets) {
        return assets.stream()
                .filter(asset -> {
                    BigDecimal price = asset.getPrice();
                    LocalDateTime timestamp = asset.getTimestamp();
                    Duration duration = Duration.between(timestamp, LocalDateTime.now());
                    return price.compareTo(new BigDecimal("10000")) > 0
                           && duration.toHours() < 24
                           && !asset.getSymbol().startsWith("TEST");
                })
                .sorted((a1, a2) -> {
                    int priceCompare = a2.getPrice().compareTo(a1.getPrice());
                    if (priceCompare != 0) return priceCompare;
                    return a1.getSymbol().compareTo(a2.getSymbol());
                })
                .collect(Collectors.toList());
    }
}
```

#### ✅ 좋은 예시 - 메서드 추출

```java
@Service
public class AssetAnalysisService {

    private static final BigDecimal PRICE_THRESHOLD = new BigDecimal("10000");
    private static final long RECENT_HOURS = 24;

    // ✅ 람다 대신 메서드 참조 사용
    public List<Asset> analyzeAssets(List<Asset> assets) {
        return assets.stream()
                .filter(this::isValidAsset)  // 메서드 참조
                .sorted(this::compareAssets)  // 메서드 참조
                .collect(Collectors.toList());
    }

    // ✅ 필터 로직을 명시적 메서드로 분리
    private boolean isValidAsset(Asset asset) {
        return isPriceAboveThreshold(asset)
               && isRecentlyUpdated(asset)
               && isNotTestAsset(asset);
    }

    private boolean isPriceAboveThreshold(Asset asset) {
        return asset.isPriceAbove(PRICE_THRESHOLD);  // 도메인 메서드 활용!
    }

    private boolean isRecentlyUpdated(Asset asset) {
        return asset.isRecentlyUpdated();  // 도메인 메서드 활용!
    }

    private boolean isNotTestAsset(Asset asset) {
        return !asset.getSymbol().startsWith("TEST");
    }

    // ✅ 정렬 로직을 명시적 메서드로 분리
    private int compareAssets(Asset a1, Asset a2) {
        int priceCompare = a2.getPrice().compareTo(a1.getPrice());
        if (priceCompare != 0) {
            return priceCompare;
        }
        return a1.getSymbol().compareTo(a2.getSymbol());
    }
}
```

### 🎯 람다 사용 가이드라인

```java
// ✅ 간단한 람다는 사용 가능
list.stream()
    .filter(x -> x > 10)  // ✅ 한 줄짜리 간단한 조건
    .map(x -> x * 2)      // ✅ 간단한 변환
    .collect(Collectors.toList());

// ✅ 메서드 참조를 최대한 활용
list.stream()
    .map(Asset::getSymbol)           // ✅ 메서드 참조
    .filter(Objects::nonNull)        // ✅ 메서드 참조
    .collect(Collectors.toList());

// ✅ 도메인 메서드 참조가 가장 좋음
assets.stream()
    .filter(Asset::isAdult)          // ✅ 도메인 메서드 참조
    .filter(Asset::isRecentlyUpdated)
    .collect(Collectors.toList());

// ❌ 복잡한 람다는 피하고 메서드로 추출
list.stream()
    .filter(asset -> {               // ❌ 여러 줄 람다
        // 복잡한 로직...
        return result;
    })
    .collect(Collectors.toList());

// ✅ 메서드로 추출
list.stream()
    .filter(this::complexCondition)  // ✅ 명시적 메서드
    .collect(Collectors.toList());
```

---

## 📋 체크리스트

새로운 코드를 작성할 때 다음을 확인하세요:

### 도메인 설계
- [ ] Builder 패턴 사용
- [ ] 필수 필드 초기화 보장
- [ ] 비교/검증 로직은 도메인 객체에 위치

### 메서드 파라미터
- [ ] String 파라미터 2개 이상 → DTO/Command 객체로 변환
- [ ] 파라미터 순서 실수 방지를 위한 타입 안정성 확보
- [ ] 명확한 의도를 나타내는 객체명 사용

### Enum 활용
- [ ] 고정된 값들의 집합은 Enum으로 정의
- [ ] Enum에 비즈니스 로직 추가
- [ ] 문자열 상수 대신 Enum 사용

### 메서드 분리
- [ ] 2곳 이상에서 사용되는 로직만 별도 메서드로 분리
- [ ] 1회성 로직은 한 곳에 모아서 가독성 유지
- [ ] 복잡한 계산, 검증, 외부 호출은 분리

### Stream & 람다
- [ ] for문 대신 Stream API 사용
- [ ] 복잡한 람다는 메서드로 추출
- [ ] 메서드 참조 적극 활용

---

이 코딩 스탠다드를 따라 일관성 있고 유지보수하기 쉬운 코드를 작성하세요! 🎯

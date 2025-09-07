# GlobalQA Automation Suite 

## 🚀 Özellikler

- **Page Object Model (POM)** - Sürdürülebilir ve yeniden kullanılabilir kod yapısı
- **Multi-Browser Support** - Chrome ve Firefox tarayıcı desteği
- **Parametrik Test Execution** - TestNG ile tarayıcı parametreli test çalıştırma
- **JSON Test Data Management** - Merkezi test veri yönetimi ve parametrik test desteği
- **Comprehensive Logging** - Log4j2 ile detaylı loglama (INFO/ERROR seviyeli)
- **Screenshot on Failure** - Test başarısız olduğunda otomatik ekran görüntüsü alma
- **WebDriverManager** - Otomatik WebDriver yönetimi
- **Fluent Wait Implementation** - Dinamik element bekleme mekanizmaları

## 🛠️ Teknoloji Stack

- **Java 21** - Modern Java özellikleri ve performans
- **Selenium WebDriver 4.34.0** - Web otomasyon framework'ü
- **TestNG 7.10.2** - Test framework'ü ve parametrik test desteği
- **Log4j2 2.23.1** - Gelişmiş loglama sistemi
- **Jackson 2.18.2** - JSON test verisi işleme
- **Commons IO 2.20.0** - Dosya işlemleri ve screenshot yönetimi
- **WebDriverManager 5.9.1** - Otomatik WebDriver yönetimi
- **Maven** - Bağımlılık yönetimi ve build otomasyonu

## 📋 Test Senaryoları

1. **Insider Ana Sayfa Doğrulama**
   - https://useinsider.com/ adresine gitme
   - Ana sayfanın açıldığını doğrulama
   - Header navigasyon menülerini kontrol etme

2. **Kariyer Sayfası Doğrulama**
   - Company menüsüne hover yaparak dropdown açma
   - Dropdown'dan Careers linkine tıklama
   - Locations, Teams ve Life at Insider bölümlerinin varlığını kontrol etme

3. **QA İş İlanları Filtreleme**
   - QA kariyer sayfasına gitme
   - "See all QA jobs" butonuna tıklama
   - Konum: "Istanbul, Turkey" ve Departman: "Quality Assurance" ile filtreleme

4. **İş İlanı Detay Doğrulama**
   - Tüm iş ilanlarının Position, Department ve Location bilgilerini kontrol etme
   - Her iş ilanının "Quality Assurance" içerdiğini doğrulama

5. **View Role Butonu Testi**
   - "View Role" butonuna tıklama
   - Lever Application form sayfasına yönlendirme doğrulama

## 📁 Proje Yapısı

```
src/
├── main/
│   └── java/
│       └── automation/com/
│           └── Main.java           # Ana uygulama sınıfı
├── test/
│   ├── java/
│   │   └── automation/
│   │       └── com/
│   │           ├── config/          # Konfigürasyon sınıfları
│   │           │   └── Config.java  # Merkezi konfigürasyon
│   │           ├── pages/           # Page Object Model sınıfları
│   │           │   ├── BasePage.java      # Temel sayfa sınıfı
│   │           │   ├── HomePage.java      # Ana sayfa
│   │           │   ├── CareersPage.java   # Kariyer sayfası
│   │           │   └── QACareersPage.java # QA kariyer sayfası
│   │           ├── tests/           # Test sınıfları
│   │           │   └── InsiderTest.java   # Ana test sınıfı
│   │           └── utils/           # Utility sınıfları
│   │               ├── BrowserManager.java # WebDriver yönetimi
│   │               ├── ScreenshotUtil.java # Ekran görüntüsü
│   │               ├── JsonReader.java     # JSON test verisi okuma
│   │               └── DebugUtil.java      # Debug yardımcıları
│   └── resources/
│       ├── testdata/                # Test verileri (JSON)
│       │   └── testdata.json        # Merkezi test veri dosyası
│       ├── screenshots/             # Ekran görüntüleri
│       └── log4j2.xml              # Log konfigürasyonu
├── logs/                            # Log dosyaları
└── testng.xml                      # TestNG konfigürasyonu
```

### Gereksinimler
- Java 21 veya üzeri
- Maven 3.6 veya üzeri
- Chrome veya Firefox tarayıcısı

**Parametrik Test Avantajları:**
- **Cross-Browser Testing**: Aynı testleri farklı tarayıcılarda çalıştırma
- **Data-Driven Testing**: JSON test verileri ile parametrik test
- **Parallel Execution**: TestNG ile paralel test çalıştırma
- **Flexible Configuration**: Runtime'da tarayıcı seçimi

## 📊 Test Raporları

Test sonuçları aşağıdaki konumlarda bulunur:
- **TestNG Reports**: `target/surefire-reports/`
- **Screenshots**: `src/test/resources/screenshots/`
- **Logs**: `logs/` klasörü

## 🔧 Konfigürasyon

### Tarayıcı Ayarları
Tarayıcı türü `Config.java` dosyasında veya test parametreleri ile değiştirilebilir:
```java
public static final String DEFAULT_BROWSER = "chrome";
```

### Test Verileri (JSON Test Data Management)

Proje, merkezi test veri yönetimi için JSON formatını kullanır. Test verileri `src/test/resources/testdata/testdata.json` dosyasında bulunur:

```json
{
  "header_tabs": {
    "main": ["Company", "Solutions", "Resources", "Pricing"],
    "subHeaders": {
      "Company": ["About Us", "Careers", "Contact"]
    }
  },
  "careers_page": {
    "expected_sections": ["Teams", "Locations", "Life at Insider"]
  },
  "qa_jobs": {
    "filters": {
      "location": "Istanbul, Turkey",
      "department": "Quality Assurance"
    },
    "expected_job_details": {
      "position_contains": "Quality Assurance",
      "department_contains": "Quality Assurance",
      "location_contains": "Istanbul, Turkey"
    }
  }
}
```

**JSON Test Data Avantajları:**
- **Merkezi Yönetim**: Tüm test verileri tek dosyada
- **Parametrik Test Desteği**: Farklı veri setleri ile test çalıştırma
- **Kolay Bakım**: Test verilerini kod değiştirmeden güncelleme
- **Veri Ayrımı**: Test mantığı ve test verilerinin ayrılması

## 📝 Loglama

Proje Log4j2 kullanarak kapsamlı loglama sağlar:
- **Console**: Test çalıştırma sırasında konsol çıktısı
- **File**: `logs/automation.log` - Tüm loglar
- **Info**: `logs/info.log` - Sadece INFO seviyeli loglar
- **Error**: `logs/error.log` - Sadece ERROR seviyeli loglar
- **Rolling**: `logs/automation-rolling.log` - Dönen log dosyası

## 🐛 Hata Ayıklama

Test başarısız olduğunda:
1. Otomatik olarak ekran görüntüsü alınır
2. Detaylı hata logları `logs/error.log` dosyasına yazılır
3. Screenshot'lar `src/test/resources/screenshots/` klasöründe saklanır

## 🔧 Proje Özellikleri Detayı

### Page Object Model (POM) Implementation
- **BasePage**: Tüm sayfa sınıfları için ortak fonksiyonlar
- **HomePage**: Ana sayfa elementleri ve işlemleri
- **CareersPage**: Kariyer sayfası elementleri ve işlemleri  
- **QACareersPage**: QA kariyer sayfası elementleri ve işlemleri

### Test Data Management
- **JsonReader**: JSON test verilerini okuma ve yönetme
- **Config**: Merkezi konfigürasyon yönetimi
- **Parametrik Test**: Farklı veri setleri ile test çalıştırma

### Utility Classes
- **BrowserManager**: WebDriver lifecycle yönetimi
- **ScreenshotUtil**: Test başarısızlığında otomatik screenshot
- **DebugUtil**: Test sırasında element debugging

### Logging & Reporting
- **Log4j2**: Çok seviyeli loglama sistemi
- **TestNG Reports**: Detaylı test raporları
- **Screenshot on Failure**: Otomatik hata görüntüleme

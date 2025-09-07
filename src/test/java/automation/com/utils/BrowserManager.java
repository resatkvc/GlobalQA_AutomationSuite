package automation.com.utils;

import automation.com.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserManager {
    
    private static final Logger logger = LogManager.getLogger(BrowserManager.class);
    private static WebDriver driver;
    
    public static WebDriver initializeDriver(String browserType) {
        try {
            if (driver != null) {
                driver.quit();
            }
            
            switch (browserType.toLowerCase()) {
                case "chrome":
                    driver = initializeChromeDriver();
                    break;
                case "firefox":
                    driver = initializeFirefoxDriver();
                    break;
                default:
                    logger.warn("Unsupported browser type: " + browserType + ". Using Chrome as default.");
                    driver = initializeChromeDriver();
                    break;
            }
            
            driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(Config.IMPLICIT_WAIT));
            driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(Config.PAGE_LOAD_TIMEOUT));
            
            driver.manage().window().maximize();
            
            logger.info("WebDriver initialized successfully for browser: " + browserType);
            return driver;
            
        } catch (Exception e) {
            logger.error("Error initializing WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }
    
    private static WebDriver initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        return new ChromeDriver(options);
    }
    
    private static WebDriver initializeFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("dom.push.enabled", false);
        
        return new FirefoxDriver(options);
    }
    
    public static WebDriver getDriver() {
        if (driver == null) {
            logger.warn("WebDriver is null. Initializing with default browser: " + Config.DEFAULT_BROWSER);
            return initializeDriver(Config.DEFAULT_BROWSER);
        }
        return driver;
    }
    
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: " + e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
    
    public static boolean isDriverInitialized() {
        return driver != null;
    }
}


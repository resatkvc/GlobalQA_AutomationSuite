package automation.com.pages;

import automation.com.config.Config;
import automation.com.utils.BrowserManager;
import automation.com.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import java.util.function.Function;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class BasePage {
    
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage() {
        this.driver = BrowserManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.EXPLICIT_WAIT));
    }
    
    public void navigateTo(String url) {
        try {
            driver.get(url);
            logger.info("Navigated to URL: " + url);
            waitForPageLoad();
            
            handleCookieBanner();
            
        } catch (Exception e) {
            logger.error("Error navigating to URL " + url + ": " + e.getMessage());
            takeScreenshot("navigation_error");
            throw new RuntimeException("Failed to navigate to URL: " + url, e);
        }
    }
    
    public void waitForPageLoad() {
        try {
            wait.until(webDriver -> 
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully");
        } catch (Exception e) {
            logger.error("Error waiting for page load: " + e.getMessage());
        }
    }
    
    public WebElement waitForElementVisible(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible: " + locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not visible: " + locator + " - " + e.getMessage());
            takeScreenshot("element_not_visible");
            throw new RuntimeException("Element not visible: " + locator, e);
        }
    }
    
    public WebElement waitForElementVisible(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible: " + locator);
            return element;
        } catch (Exception e) {
            logger.debug("Element not found within " + timeoutSeconds + " seconds: " + locator);
            return null;
        }
    }
    
    public WebElement waitForElementClickable(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element is clickable: " + locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not clickable: " + locator + " - " + e.getMessage());
            takeScreenshot("element_not_clickable");
            throw new RuntimeException("Element not clickable: " + locator, e);
        }
    }
    
    public void clickElement(By locator) {
        try {
            WebElement element = waitForElementClickable(locator);
            

            
            try {

                element.click();
                logger.info("Clicked on element using normal click: " + locator);
            } catch (Exception e1) {
                logger.warn("Normal click failed, trying JavaScript click: " + locator);
                try {

                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    logger.info("Clicked on element using JavaScript: " + locator);
                } catch (Exception e2) {
                    logger.warn("JavaScript click failed, trying Actions click: " + locator);
                    try {

                        org.openqa.selenium.interactions.Actions actions = 
                            new org.openqa.selenium.interactions.Actions(driver);
                        actions.moveToElement(element).click().perform();
                        logger.info("Clicked on element using Actions: " + locator);
                    } catch (Exception e3) {
                        logger.warn("Actions click failed, trying force click: " + locator);
                        try {

                            ((JavascriptExecutor) driver)
                                .executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", element);
                            logger.info("Clicked on element using force click: " + locator);
                        } catch (Exception e4) {
                            logger.error("All click methods failed for element: " + locator);
                            takeScreenshot("click_error");
                            throw new RuntimeException("Failed to click element: " + locator, e4);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Error clicking element: " + locator + " - " + e.getMessage());
            takeScreenshot("click_error");
            throw new RuntimeException("Failed to click element: " + locator, e);
        }
    }
    
    
    public void sendText(By locator, String text) {
        try {
            WebElement element = waitForElementVisible(locator);
            element.clear();
            element.sendKeys(text);
            logger.info("Sent text to element: " + locator + " - Text: " + text);
        } catch (Exception e) {
            logger.error("Error sending text to element: " + locator + " - " + e.getMessage());
            takeScreenshot("send_text_error");
            throw new RuntimeException("Failed to send text to element: " + locator, e);
        }
    }
    
    
    public String getElementText(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            String text = element.getText();
            logger.info("Got text from element: " + locator + " - Text: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Error getting text from element: " + locator + " - " + e.getMessage());
            takeScreenshot("get_text_error");
            throw new RuntimeException("Failed to get text from element: " + locator, e);
        }
    }
    
    
    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean isDisplayed = element.isDisplayed();
            logger.info("Element display status: " + locator + " - Displayed: " + isDisplayed);
            return isDisplayed;
        } catch (NoSuchElementException e) {
            logger.info("Element not found: " + locator);
            return false;
        } catch (Exception e) {
            logger.error("Error checking element display: " + locator + " - " + e.getMessage());
            return false;
        }
    }
    
    
    public List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            logger.info("Found " + elements.size() + " elements for locator: " + locator);
            return elements;
        } catch (Exception e) {
            logger.error("Error finding elements: " + locator + " - " + e.getMessage());
            return null;
        }
    }
    
    
    public void scrollToElement(By locator) {
        try {
            WebElement element = waitForElementVisible(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: " + locator);
        } catch (Exception e) {
            logger.error("Error scrolling to element: " + locator + " - " + e.getMessage());
        }
    }
    
    
    public void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: " + element);
        } catch (Exception e) {
            logger.error("Error scrolling to element: " + element + " - " + e.getMessage());
        }
    }
    
    
    public void verifyPageTitle(String expectedTitle) {
        try {
            String actualTitle = driver.getTitle();
            logger.info("Page title verification - Expected: " + expectedTitle + ", Actual: " + actualTitle);
            Assert.assertTrue(actualTitle.contains(expectedTitle), 
                "Page title verification failed. Expected: " + expectedTitle + ", Actual: " + actualTitle);
        } catch (Exception e) {
            logger.error("Error verifying page title: " + e.getMessage());
            takeScreenshot("title_verification_error");
            throw e;
        }
    }
    
    
    public void verifyCurrentUrl(String expectedUrl) {
        try {
            String actualUrl = driver.getCurrentUrl();
            logger.info("URL verification - Expected: " + expectedUrl + ", Actual: " + actualUrl);
            Assert.assertTrue(actualUrl.contains(expectedUrl), 
                "URL verification failed. Expected: " + expectedUrl + ", Actual: " + actualUrl);
        } catch (Exception e) {
            logger.error("Error verifying URL: " + e.getMessage());
            takeScreenshot("url_verification_error");
            throw e;
        }
    }
    
    
    protected void takeScreenshot(String message) {
        ScreenshotUtil.takeScreenshotWithMessage(driver, message);
    }
    
    
    @Deprecated
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.info("Waited for " + seconds + " seconds");
        } catch (InterruptedException e) {
            logger.error("Error waiting: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    
    public WebElement waitForElementClickable(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = customWait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element is clickable: " + locator);
            return element;
        } catch (Exception e) {
            logger.debug("Element not clickable within " + timeoutSeconds + " seconds: " + locator);
            return null;
        }
    }
    
    
    public WebElement waitForElementPresent(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.info("Element is present: " + locator);
            return element;
        } catch (Exception e) {
            logger.debug("Element not present within " + timeoutSeconds + " seconds: " + locator);
            return null;
        }
    }
    
    
    public boolean waitForTextInElement(By locator, String text, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean result = customWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            logger.info("Text '" + text + "' is present in element: " + locator);
            return result;
        } catch (Exception e) {
            logger.debug("Text '" + text + "' not present in element within " + timeoutSeconds + " seconds: " + locator);
            return false;
        }
    }
    
    
    public boolean waitForElementInvisible(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean result = customWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.info("Element is invisible: " + locator);
            return result;
        } catch (Exception e) {
            logger.debug("Element still visible after " + timeoutSeconds + " seconds: " + locator);
            return false;
        }
    }
    
    
    public List<WebElement> waitForElementsCount(By locator, int expectedCount, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            List<WebElement> elements = customWait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
            logger.info("Found " + elements.size() + " elements: " + locator);
            return elements;
        } catch (Exception e) {
            logger.debug("Expected " + expectedCount + " elements not found within " + timeoutSeconds + " seconds: " + locator);
            return null;
        }
    }
    
    
    public boolean waitForElementAttribute(By locator, String attribute, String value, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean result = customWait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
            logger.info("Element attribute '" + attribute + "' has value '" + value + "': " + locator);
            return result;
        } catch (Exception e) {
            logger.debug("Element attribute '" + attribute + "' does not have value '" + value + "' within " + timeoutSeconds + " seconds: " + locator);
            return false;
        }
    }
    
    
    public boolean waitForElementAttribute(WebElement element, String attribute, String value, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean result = customWait.until(ExpectedConditions.attributeToBe(element, attribute, value));
            logger.info("Element attribute '" + attribute + "' has value '" + value + "'");
            return result;
        } catch (Exception e) {
            logger.debug("Element attribute '" + attribute + "' does not have value '" + value + "' within " + timeoutSeconds + " seconds");
            return false;
        }
    }
    
    
    public WebElement waitForElementClickable(WebElement element, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement result = customWait.until(ExpectedConditions.elementToBeClickable(element));
            logger.info("Element is clickable");
            return result;
        } catch (Exception e) {
            logger.debug("Element not clickable within " + timeoutSeconds + " seconds");
            return null;
        }
    }
    
    
    public <T> T fluentWait(Function<WebDriver, T> condition, int timeoutSeconds, int pollingIntervalSeconds) {
        try {
            Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofSeconds(pollingIntervalSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
            
            T result = fluentWait.until(condition);
            logger.info("Fluent wait condition met");
            return result;
        } catch (Exception e) {
            logger.debug("Fluent wait condition not met within " + timeoutSeconds + " seconds: " + e.getMessage());
            return null;
        }
    }
    
    
    public boolean waitForPageLoad(int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean result = customWait.until(webDriver -> 
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully");
            return result;
        } catch (Exception e) {
            logger.error("Error waiting for page load: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean waitForJavaScript(String javascript, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean result = customWait.until(webDriver -> 
                (Boolean) ((JavascriptExecutor) webDriver).executeScript(javascript));
            logger.info("JavaScript condition met: " + javascript);
            return result;
        } catch (Exception e) {
            logger.debug("JavaScript condition not met within " + timeoutSeconds + " seconds: " + javascript);
            return false;
        }
    }
    
    
    public void handleCookieBanner() {
        try {
            logger.info("Checking for cookie banner");
            

            By cookieBanner = By.id("cookie-law-info-bar");
            By acceptAllButton = By.id("wt-cli-accept-all-btn");
            By acceptAllButtonAlternative = By.xpath("//a[contains(@class, 'wt-cli-accept-all-btn') or contains(text(), 'Accept All')]");
            

            if (isElementDisplayed(cookieBanner)) {
                logger.info("Cookie banner found, attempting to click 'Accept All' button");
                

                WebElement acceptButton = null;
                
                try {
                    if (waitForElementVisible(acceptAllButton, 5) != null) {
                        acceptButton = driver.findElement(acceptAllButton);
                        logger.info("Found Accept All button with ID locator");
                    }
                } catch (Exception e) {
                    logger.debug("Accept All button not found with ID locator, trying alternative");
                }
                
                if (acceptButton == null) {
                    try {
                        if (waitForElementVisible(acceptAllButtonAlternative, 5) != null) {
                            acceptButton = driver.findElement(acceptAllButtonAlternative);
                            logger.info("Found Accept All button with alternative locator");
                        }
                    } catch (Exception e) {
                        logger.debug("Accept All button not found with alternative locator");
                    }
                }
                
                if (acceptButton != null) {
                    try {

                        scrollToElement(acceptButton);
                        waitForSeconds(1);
                        

                        acceptButton.click();
                        logger.info("Successfully clicked 'Accept All' button");
                        

                        waitForSeconds(2);
                        
                    } catch (Exception e) {
                        logger.warn("Normal click failed, trying JavaScript click");
                        try {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
                            logger.info("Successfully clicked 'Accept All' button using JavaScript");
                            waitForSeconds(2);
                        } catch (Exception e2) {
                            logger.error("Failed to click Accept All button: " + e2.getMessage());
                        }
                    }
                } else {
                    logger.warn("Accept All button not found in cookie banner");
                }
            } else {
                logger.info("No cookie banner found");
            }
            
        } catch (Exception e) {
            logger.warn("Error handling cookie banner: " + e.getMessage());

        }
    }
}


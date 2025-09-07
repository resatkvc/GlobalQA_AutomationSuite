package automation.com.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DebugUtil {
    
    private static final Logger logger = LogManager.getLogger(DebugUtil.class);
    
    
    public static void printElementsByTag(WebDriver driver, String tagName) {
        try {
            List<WebElement> elements = driver.findElements(By.tagName(tagName));
            logger.info("Found " + elements.size() + " " + tagName + " elements:");
            
            for (int i = 0; i < elements.size(); i++) {
                WebElement element = elements.get(i);
                String text = element.getText().trim();
                String className = element.getAttribute("class");
                String id = element.getAttribute("id");
                
                if (!text.isEmpty()) {
                    logger.info("  " + (i + 1) + ". Text: '" + text + "' | Class: '" + className + "' | ID: '" + id + "'");
                }
            }
        } catch (Exception e) {
            logger.error("Error printing elements by tag " + tagName + ": " + e.getMessage());
        }
    }
    
    
    public static void printElementsContainingText(WebDriver driver, String searchText) {
        try {
            By locator = By.xpath("/
    public static void printPageInfo(WebDriver driver) {
        try {
            String title = driver.getTitle();
            String url = driver.getCurrentUrl();
            logger.info("Page Info - Title: '" + title + "' | URL: '" + url + "'");
        } catch (Exception e) {
            logger.error("Error printing page info: " + e.getMessage());
        }
    }
    
    
    public static void printAllHeadings(WebDriver driver) {
        try {
            logger.info("=== ALL HEADINGS ON PAGE ===");
            printElementsByTag(driver, "h1");
            printElementsByTag(driver, "h2");
            printElementsByTag(driver, "h3");
            printElementsByTag(driver, "h4");
            printElementsByTag(driver, "h5");
            printElementsByTag(driver, "h6");
        } catch (Exception e) {
            logger.error("Error printing all headings: " + e.getMessage());
        }
    }
    
    
    public static void printSectionsAndDivs(WebDriver driver) {
        try {
            logger.info("=== SECTIONS AND DIVS ===");
            printElementsByTag(driver, "section");
            printElementsByTag(driver, "div");
        } catch (Exception e) {
            logger.error("Error printing sections and divs: " + e.getMessage());
        }
    }
    
    
    public static void debugCareersPage(WebDriver driver) {
        try {
            logger.info("=== DEBUGGING CAREERS PAGE ===");
            printPageInfo(driver);
            printAllHeadings(driver);
            printElementsContainingText(driver, "careers");
            printElementsContainingText(driver, "location");
            printElementsContainingText(driver, "team");
            printElementsContainingText(driver, "life");
        } catch (Exception e) {
            logger.error("Error debugging careers page: " + e.getMessage());
        }
    }
}


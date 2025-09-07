package automation.com.utils;

import automation.com.config.Config;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);
    
    public static String takeScreenshot(WebDriver driver, String fileName) {
        try {
            File screenshotDir = new File(Config.SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
                logger.info("Created screenshots directory: " + Config.SCREENSHOT_PATH);
            }
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fullFileName = fileName + "_" + timestamp + ".png";
            String filePath = Config.SCREENSHOT_PATH + fullFileName;
            
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(filePath);
            
            FileUtils.copyFile(sourceFile, destinationFile);
            
            logger.info("Screenshot saved successfully: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            logger.error("Error taking screenshot: " + e.getMessage());
            return null;
        }
    }
    
    public static String takeScreenshotForFailedTest(WebDriver driver, ITestResult testResult) {
        String testName = testResult.getMethod().getMethodName();
        String className = testResult.getTestClass().getName();
        String fileName = className + "_" + testName + "_FAILED";
        
        logger.info("Taking screenshot for failed test: " + testName);
        return takeScreenshot(driver, fileName);
    }
    
    public static String takeScreenshotWithMessage(WebDriver driver, String message) {
        String fileName = "Screenshot_" + message.replaceAll("[^a-zA-Z0-9]", "_");
        logger.info("Taking screenshot with message: " + message);
        return takeScreenshot(driver, fileName);
    }
    
    public static void cleanOldScreenshots(int days) {
        try {
            File screenshotDir = new File(Config.SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                return;
            }
            
            File[] files = screenshotDir.listFiles();
            if (files == null) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);
            int deletedCount = 0;
            
            for (File file : files) {
                if (file.isFile() && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deletedCount++;
                    }
                }
            }
            
            logger.info("Cleaned " + deletedCount + " old screenshots");
            
        } catch (Exception e) {
            logger.error("Error cleaning old screenshots: " + e.getMessage());
        }
    }
}


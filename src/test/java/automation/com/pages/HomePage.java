package automation.com.pages;

import automation.com.config.Config;
import automation.com.utils.JsonReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage extends BasePage {
    

    private static final By COMPANY_MENU = By.xpath("//a[contains(text(), 'Company')]");
    private static final By CAREERS_LINK = By.xpath("//a[contains(text(), 'Careers') and contains(@href, 'careers')]");
    private static final By CAREERS_LINK_ALTERNATIVE = By.xpath("//a[text()='Careers']");
    private static final By CAREERS_LINK_DROPDOWN = By.xpath("//a[contains(@class, 'dropdown-sub') and contains(text(), 'Careers')]");
    private static final By CAREERS_LINK_SIMPLE = By.xpath("//a[contains(text(), 'Careers')]");
    private static final By INSIDER_LOGO = By.xpath("//img[contains(@alt, 'Insider') or contains(@src, 'insider')]");
    private static final By MAIN_NAVIGATION = By.xpath("//nav[contains(@class, 'navbar') or contains(@class, 'navigation')]");
    private static final By HEADER_TABS = By.xpath("//ul[contains(@class, 'navbar-nav') or contains(@class, 'nav-menu')]//a");
    private static final By COMPANY_DROPDOWN = By.xpath("//div[contains(@class, 'new-menu-dropdown-layout-6-mid-container')]");
    
    public HomePage() {
        super();
    }
    
    public void navigateToHomePage() {
        navigateTo(Config.BASE_URL);
        verifyPageTitle("Insider");
        verifyCurrentUrl("useinsider.com");
        logger.info("Successfully navigated to Insider home page");
    }
    
    
    public boolean verifyHomePageOpened() {
        try {

            boolean logoPresent = isElementDisplayed(INSIDER_LOGO);
            

            boolean navigationPresent = isElementDisplayed(MAIN_NAVIGATION);
            

            boolean companyMenuPresent = isElementDisplayed(COMPANY_MENU);
            
            boolean isHomePageOpened = logoPresent && navigationPresent && companyMenuPresent;
            
            logger.info("Home page verification - Logo: " + logoPresent + 
                       ", Navigation: " + navigationPresent + 
                       ", Company Menu: " + companyMenuPresent);
            
            return isHomePageOpened;
            
        } catch (Exception e) {
            logger.error("Error verifying home page: " + e.getMessage());
            takeScreenshot("homepage_verification_error");
            return false;
        }
    }
    
    
    public boolean verifyHeaderNavigationTabs() {
        try {
            List<String> expectedTabs = JsonReader.getMainHeaderTabs();
            List<WebElement> actualTabs = getElements(HEADER_TABS);
            
            if (actualTabs == null || actualTabs.isEmpty()) {
                logger.error("No navigation tabs found");
                return false;
            }
            
            boolean allTabsPresent = true;
            for (String expectedTab : expectedTabs) {
                boolean tabFound = false;
                for (WebElement tab : actualTabs) {
                    if (tab.getText().trim().equals(expectedTab)) {
                        tabFound = true;
                        break;
                    }
                }
                if (!tabFound) {
                    logger.warn("Expected tab not found: " + expectedTab);
                    allTabsPresent = false;
                }
            }
            
            logger.info("Header navigation tabs verification completed. All tabs present: " + allTabsPresent);
            return allTabsPresent;
            
        } catch (Exception e) {
            logger.error("Error verifying header navigation tabs: " + e.getMessage());
            takeScreenshot("header_tabs_verification_error");
            return false;
        }
    }
    
    
    public void hoverOverCompanyMenu() {
        try {
            WebElement companyMenu = waitForElementVisible(COMPANY_MENU);
            

            try {

                org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                actions.moveToElement(companyMenu).perform();
                logger.info("Hovered over Company menu using Actions");
            } catch (Exception e1) {
                try {

                    ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));", companyMenu);
                    logger.info("Hovered over Company menu using JavaScript");
                } catch (Exception e2) {

                    companyMenu.click();
                    logger.info("Clicked Company menu to open dropdown");
                }
            }
            

            waitForElementVisible(COMPANY_DROPDOWN, Config.SHORT_WAIT);
            

            boolean dropdownVisible = isElementDisplayed(COMPANY_DROPDOWN);
            if (!dropdownVisible) {
                logger.warn("Company dropdown might not be visible, trying alternative approach");
            }
            
        } catch (Exception e) {
            logger.error("Error hovering over Company menu: " + e.getMessage());
            takeScreenshot("hover_company_menu_error");
            throw new RuntimeException("Failed to hover over Company menu", e);
        }
    }
    
    
    public CareersPage clickCareersLink() {
        try {
            logger.info("Looking for Careers link in Company dropdown");
            

            By[] careersLocators = {CAREERS_LINK, CAREERS_LINK_ALTERNATIVE, CAREERS_LINK_DROPDOWN, CAREERS_LINK_SIMPLE};
            WebElement careersLink = null;
            
            for (By locator : careersLocators) {
                try {
                    if (waitForElementVisible(locator) != null) {
                        careersLink = driver.findElement(locator);
                        logger.info("Found Careers link with locator: " + locator);
                        break;
                    }
                } catch (Exception e) {
                    logger.debug("Careers link not found with locator: " + locator);
                }
            }
            
            if (careersLink == null) {
                logger.error("Careers link not found with any locator");
                takeScreenshot("careers_link_not_found");
                throw new RuntimeException("Careers link not found");
            }
            

            scrollToElement(careersLink);
            waitForElementClickable(careersLink, Config.SHORT_WAIT);
            

            try {
                careersLink.click();
                logger.info("Successfully clicked on Careers link");
            } catch (Exception e) {
                logger.warn("Normal click failed, trying JavaScript click");
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", careersLink);
                logger.info("Successfully clicked on Careers link using JavaScript");
            }
            

            waitForPageLoad(Config.PAGE_LOAD_TIMEOUT);
            
            return new CareersPage();
        } catch (Exception e) {
            logger.error("Error clicking Careers link: " + e.getMessage());
            takeScreenshot("click_careers_error");
            throw new RuntimeException("Failed to click Careers link", e);
        }
    }
    
    
    public CareersPage navigateToCareersPage() {
        try {
            logger.info("Starting navigation to Careers page via Company menu");
            

            hoverOverCompanyMenu();
            

            CareersPage careersPage = clickCareersLink();
            
            logger.info("Successfully navigated to Careers page via Company menu");
            return careersPage;
            
        } catch (Exception e) {
            logger.error("Error navigating to Careers page: " + e.getMessage());
            takeScreenshot("navigate_careers_error");
            throw new RuntimeException("Failed to navigate to Careers page", e);
        }
    }
    
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}


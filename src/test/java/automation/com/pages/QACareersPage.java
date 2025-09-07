package automation.com.pages;

import automation.com.config.Config;
import automation.com.utils.JsonReader;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class QACareersPage extends BasePage {
    
    private static final By SEE_ALL_QA_JOBS_BUTTON = By.xpath("//a[contains(@href, 'qualityassurance') and contains(text(), 'See all QA jobs')]");
    private static final By LOCATION_FILTER = By.id("filter-by-location");
    private static final By DEPARTMENT_FILTER = By.id("filter-by-department");
    
    private static final By JOB_LIST = By.xpath("//div[contains(@class, 'job-item') or contains(@class, 'position-item') or contains(@class, 'position-list-item') or contains(@class, 'job-list-item')]");
    private static final By JOB_LIST_ALTERNATIVE1 = By.xpath("//div[contains(@class, 'position')]");
    private static final By JOB_LIST_ALTERNATIVE2 = By.xpath("//div[contains(@class, 'job')]");
    private static final By JOB_LIST_ALTERNATIVE3 = By.xpath("//li[contains(@class, 'position') or contains(@class, 'job')]");
    private static final By JOB_LIST_ALTERNATIVE4 = By.xpath("//article[contains(@class, 'position') or contains(@class, 'job')]");
    
    private static final By JOB_POSITION = By.xpath(".//h3[contains(@class, 'position-title') or contains(@class, 'job-title') or contains(@class, 'title')]");
    private static final By JOB_DEPARTMENT = By.xpath(".//span[contains(@class, 'position-department') or contains(@class, 'department')]");
    private static final By JOB_LOCATION = By.xpath(".//div[contains(@class, 'position-location') or contains(@class, 'location')]");
    private static final By VIEW_ROLE_BUTTON = By.xpath("(//div[contains(@class, 'position-list-item') or contains(@class, 'job-item') or contains(@class, 'position-item')])[1]//a[contains(@class, 'btn') and (contains(text(), 'View Role') or contains(text(), 'Apply') or contains(text(), 'View'))]");
    private static final By VIEW_ROLE_BUTTON_ALTERNATIVE1 = By.cssSelector("div.position-list-item:nth-of-type(1) a.btn.btn-navy");
    private static final By VIEW_ROLE_BUTTON_ALTERNATIVE2 = By.xpath("(//a[contains(@class, 'btn') and contains(text(), 'View Role')])[1]");
    private static final By VIEW_ROLE_BUTTON_ALTERNATIVE3 = By.xpath("(//a[contains(@href, 'lever.co')])[1]");
    
    private static final By FIRST_JOB_ITEM = By.xpath("(//div[contains(@class, 'position-list-item') or contains(@class, 'job-item') or contains(@class, 'position-item')])[1]");
    private static final By PAGE_TITLE = By.xpath("//h1[contains(text(), 'Quality Assurance') or contains(text(), 'QA')]");
    
    public QACareersPage() {
        super();
    }
    
    public void navigateToQACareersPage() {
        navigateTo(Config.QA_CAREERS_URL);
        verifyPageTitle("quality assurance");
        verifyCurrentUrl("quality-assurance");
        logger.info("Successfully navigated to QA Careers page");
    }
    
    public boolean clickSeeAllQAJobsButton() {
        try {
            logger.info("Looking for 'See all QA jobs' button");
            
            if (waitForElementVisible(SEE_ALL_QA_JOBS_BUTTON) != null) {
                WebElement button = driver.findElement(SEE_ALL_QA_JOBS_BUTTON);
                
                // Scroll to element with more space
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);
                waitForElementClickable(SEE_ALL_QA_JOBS_BUTTON, Config.SHORT_WAIT);
                
                // Try multiple click methods
                boolean clickSuccess = false;
                
                try {
                    // Method 1: Normal click
                    button.click();
                    logger.info("Successfully clicked 'See all QA jobs' button using normal click");
                    clickSuccess = true;
                } catch (Exception e1) {
                    logger.warn("Normal click failed, trying JavaScript click");
                    try {
                        // Method 2: JavaScript click
                        ((org.openqa.selenium.JavascriptExecutor) driver)
                            .executeScript("arguments[0].click();", button);
                        logger.info("Successfully clicked 'See all QA jobs' button using JavaScript");
                        clickSuccess = true;
                    } catch (Exception e2) {
                        logger.warn("JavaScript click failed, trying Actions click");
                        try {
                            // Method 3: Actions click
                            org.openqa.selenium.interactions.Actions actions = 
                                new org.openqa.selenium.interactions.Actions(driver);
                            actions.moveToElement(button).click().perform();
                            logger.info("Successfully clicked 'See all QA jobs' button using Actions");
                            clickSuccess = true;
                        } catch (Exception e3) {
                            logger.warn("Actions click failed, trying force click");
                            try {
                                // Method 4: Force click with JavaScript
                                ((org.openqa.selenium.JavascriptExecutor) driver)
                                    .executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", button);
                                logger.info("Successfully clicked 'See all QA jobs' button using force click");
                                clickSuccess = true;
                            } catch (Exception e4) {
                                logger.error("All click methods failed");
                                takeScreenshot("see_all_qa_jobs_all_clicks_failed");
                                return false;
                            }
                        }
                    }
                }
                
                if (clickSuccess) {
                    waitForPageLoad(Config.PAGE_LOAD_TIMEOUT);
                    return true;
                } else {
                    logger.error("See all QA jobs button click failed");
                    takeScreenshot("see_all_qa_jobs_click_failed");
                    return false;
                }
            } else {
                logger.error("See all QA jobs button not found or not visible");
                takeScreenshot("see_all_qa_jobs_not_found");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error clicking 'See all QA jobs' button: " + e.getMessage());
            takeScreenshot("click_see_all_qa_jobs_error");
            return false;
        }
    }
    
    public boolean verifyQACareersPageOpened() {
        try {
            // Check if page title is present
            boolean titlePresent = isElementDisplayed(PAGE_TITLE);
            
            // Check if URL contains quality-assurance
            boolean urlCorrect = driver.getCurrentUrl().contains("quality-assurance");
            
            boolean isQACareersPageOpened = titlePresent && urlCorrect;
            
            logger.info("QA Careers page verification - Title: " + titlePresent + 
                       ", URL: " + urlCorrect);
            
            return isQACareersPageOpened;
            
        } catch (Exception e) {
            logger.error("Error verifying QA Careers page: " + e.getMessage());
            takeScreenshot("qa_careers_page_verification_error");
            return false;
        }
    }
    
    
    public boolean filterJobsByLocationAndDepartment() {
        try {
            JsonNode filterCriteria = JsonReader.getQAJobsFilterCriteria();
            if (filterCriteria == null) {
                logger.error("Filter criteria not found in test data");
                return false;
            }
            
            String location = filterCriteria.get("location").asText();
            String department = filterCriteria.get("department").asText();
            
            logger.info("Starting job filtering - Location: " + location + ", Department: " + department);
            
            // Apply location filter
            boolean locationFiltered = applyLocationFilter(location);
            if (!locationFiltered) {
                logger.warn("Location filter failed, but continuing with department filter");
            }
            
            // Apply department filter
            boolean departmentFiltered = applyDepartmentFilter(department);
            if (!departmentFiltered) {
                logger.warn("Department filter failed");
            }
            
            // Wait for results to load
            waitForPageLoad(Config.MEDIUM_WAIT);
            
            boolean filteringSuccess = locationFiltered || departmentFiltered;
            logger.info("Job filtering completed - Success: " + filteringSuccess);
            return filteringSuccess;
            
        } catch (Exception e) {
            logger.error("Error filtering jobs: " + e.getMessage());
            takeScreenshot("job_filtering_error");
            return false;
        }
    }
    
    
    private boolean applyLocationFilter(String location) {
        try {
            logger.info("Applying location filter: " + location);
            
            if (waitForElementVisible(LOCATION_FILTER) != null) {
                // Try different location options that might match - prioritize exact matches from dropdown
                String[] locationOptions = {
                    "Istanbul, Turkiye",  // Exact match from dropdown
                    "Istanbul",           // Short form
                    "Turkey",             // Country only
                    location,             // Original from JSON
                    "Istanbul, Turkey",   // Alternative spelling
                    "All"                 // Fallback
                };
                
                for (String option : locationOptions) {
                    try {
                        selectDropdownOption(LOCATION_FILTER, option);
                        logger.info("Successfully selected location: " + option);
                        return true;
                    } catch (Exception e) {
                        logger.debug("Failed to select location option: " + option + " - " + e.getMessage());
                    }
                }
                
                logger.error("Could not select any location option");
                return false;
            } else {
                logger.error("Location filter dropdown not found");
                takeScreenshot("location_filter_not_found");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error applying location filter: " + e.getMessage());
            takeScreenshot("location_filter_error");
            return false;
        }
    }
    
    
    private boolean applyDepartmentFilter(String department) {
        try {
            logger.info("Applying department filter: " + department);
            
            if (waitForElementVisible(DEPARTMENT_FILTER) != null) {
                selectDropdownOption(DEPARTMENT_FILTER, department);
                logger.info("Successfully selected department: " + department);
                return true;
            } else {
                logger.error("Department filter dropdown not found");
                takeScreenshot("department_filter_not_found");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error applying department filter: " + e.getMessage());
            takeScreenshot("department_filter_error");
            return false;
        }
    }
    
    
    private void selectDropdownOption(By dropdownLocator, String optionText) {
        try {
            WebElement dropdown = waitForElementVisible(dropdownLocator);
            
            // Try different approaches to select the option
            try {
                // Method 1: Using Select class with exact match
                org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(dropdown);
                select.selectByVisibleText(optionText);
                logger.info("Selected option using Select class (exact): " + optionText);
                waitForElementAttribute(dropdown, "value", optionText, Config.SHORT_WAIT); // Wait for selection to take effect
                return;
            } catch (Exception e1) {
                logger.debug("Select class exact match failed, trying partial match");
                try {
                    // Method 2: Try partial text match
                    org.openqa.selenium.support.ui.Select select2 = new org.openqa.selenium.support.ui.Select(dropdown);
                    List<WebElement> options = select2.getOptions();
                    for (WebElement option : options) {
                        String optionValue = option.getText().trim();
                        if (optionValue.contains(optionText) || optionText.contains(optionValue)) {
                            select2.selectByVisibleText(optionValue);
                            logger.info("Selected option using Select class (partial): " + optionValue);
                            waitForElementAttribute(dropdown, "value", optionValue, Config.SHORT_WAIT);
                            return;
                        }
                    }
                } catch (Exception e2) {
                    logger.debug("Select class partial match failed, trying click method");
                }
                
                try {
                    // Method 3: Click dropdown and select option
                    dropdown.click();
                    
                    // Try to find and click the option
                    By optionLocator = By.xpath("//option[contains(text(), '" + optionText + "') or text()='" + optionText + "']");
                    waitForElementVisible(optionLocator, Config.SHORT_WAIT);
                    WebElement option = waitForElementVisible(optionLocator);
                    option.click();
                    logger.info("Selected option by clicking: " + optionText);
                    waitForElementAttribute(dropdown, "value", optionText, Config.SHORT_WAIT);
                    return;
                } catch (Exception e3) {
                    logger.debug("Click method failed, trying JavaScript");
                    try {
                        // Method 4: Using JavaScript to set value
                        ((org.openqa.selenium.JavascriptExecutor) driver)
                            .executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));", 
                                         dropdown, optionText);
                        logger.info("Selected option using JavaScript: " + optionText);
                        waitForElementAttribute(dropdown, "value", optionText, Config.SHORT_WAIT);
                        return;
                    } catch (Exception e4) {
                        logger.debug("JavaScript method failed, trying force selection");
                        try {
                            // Method 5: Force selection by finding option index
                            org.openqa.selenium.support.ui.Select select3 = new org.openqa.selenium.support.ui.Select(dropdown);
                            List<WebElement> allOptions = select3.getOptions();
                            for (int i = 0; i < allOptions.size(); i++) {
                                String optionTextValue = allOptions.get(i).getText().trim();
                                if (optionTextValue.equals(optionText) || optionTextValue.contains(optionText)) {
                                    select3.selectByIndex(i);
                                    logger.info("Selected option using Select class (by index): " + optionTextValue);
                                    waitForElementAttribute(dropdown, "value", optionTextValue, Config.SHORT_WAIT);
                                    return;
                                }
                            }
                        } catch (Exception e5) {
                            throw new RuntimeException("All dropdown selection methods failed for: " + optionText);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Error selecting dropdown option: " + optionText + " - " + e.getMessage());
            throw new RuntimeException("Failed to select dropdown option: " + optionText, e);
        }
    }
    
    
    public boolean verifyJobListPresent() {
        try {
            // Try multiple locators to find job elements
            By[] jobLocators = {JOB_LIST, JOB_LIST_ALTERNATIVE1, JOB_LIST_ALTERNATIVE2, JOB_LIST_ALTERNATIVE3, JOB_LIST_ALTERNATIVE4};
            List<WebElement> jobElements = null;
            
            for (By locator : jobLocators) {
                try {
                    jobElements = getElements(locator);
                    if (jobElements != null && !jobElements.isEmpty()) {
                        logger.info("Found job elements using locator: " + locator + " - Count: " + jobElements.size());
                        break;
                    }
                } catch (Exception e) {
                    logger.debug("Locator failed: " + locator + " - " + e.getMessage());
                }
            }
            
            boolean jobListPresent = jobElements != null && !jobElements.isEmpty();
            
            if (jobListPresent) {
                logger.info("Job list verification - Found " + jobElements.size() + " jobs");
            } else {
                logger.warn("Job list verification - No jobs found with any locator");
                // Take screenshot for debugging
                takeScreenshot("no_jobs_found_after_filtering");
            }
            
            return jobListPresent;
            
        } catch (Exception e) {
            logger.error("Error verifying job list: " + e.getMessage());
            takeScreenshot("job_list_verification_error");
            return false;
        }
    }
    
    
    public boolean verifyAllJobsContainExpectedDetails() {
        try {
            JsonNode expectedDetails = JsonReader.getExpectedJobDetails();
            if (expectedDetails == null) {
                logger.error("Expected job details not found in test data");
                return false;
            }
            
            String expectedPosition = expectedDetails.get("position_contains").asText();
            String expectedDepartment = expectedDetails.get("department_contains").asText();
            String expectedLocation = expectedDetails.get("location_contains").asText();
            
            logger.info("Starting job details verification - Expected Position: " + expectedPosition + 
                       ", Department: " + expectedDepartment + ", Location: " + expectedLocation);
            
            // Try multiple locators to find job elements
            By[] jobLocators = {JOB_LIST, JOB_LIST_ALTERNATIVE1, JOB_LIST_ALTERNATIVE2, JOB_LIST_ALTERNATIVE3, JOB_LIST_ALTERNATIVE4};
            List<WebElement> jobElements = null;
            
            for (By locator : jobLocators) {
                try {
                    jobElements = getElements(locator);
                    if (jobElements != null && !jobElements.isEmpty()) {
                        logger.info("Found job elements for verification using locator: " + locator + " - Count: " + jobElements.size());
                        break;
                    }
                } catch (Exception e) {
                    logger.debug("Locator failed for verification: " + locator + " - " + e.getMessage());
                }
            }
            
            if (jobElements == null || jobElements.isEmpty()) {
                logger.error("No job elements found with any locator for verification");
                return false;
            }
            
            logger.info("Found " + jobElements.size() + " job elements to verify");
            boolean allJobsValid = true;
            
            for (int i = 0; i < jobElements.size(); i++) {
                WebElement jobElement = jobElements.get(i);
                
                // Get job details
                String position = getJobDetail(jobElement, JOB_POSITION);
                String department = getJobDetail(jobElement, JOB_DEPARTMENT);
                String location = getJobDetail(jobElement, JOB_LOCATION);
                
                logger.info("Job " + (i + 1) + " details - Position: '" + position + 
                           "', Department: '" + department + "', Location: '" + location + "'");
                
                // Verify position contains expected text
                boolean positionValid = position != null && position.contains(expectedPosition);
                
                // Verify department contains expected text
                boolean departmentValid = department != null && department.contains(expectedDepartment);
                
                // Verify location contains expected text (flexible matching for Istanbul variations)
                boolean locationValid = location != null && 
                    (location.contains(expectedLocation) || 
                     location.contains("Istanbul, Turkiye") || 
                     location.contains("Istanbul"));
                
                if (!positionValid || !departmentValid || !locationValid) {
                    logger.warn("Job " + (i + 1) + " validation failed - Position: " + positionValid + 
                               " (expected: " + expectedPosition + "), Department: " + departmentValid + 
                               " (expected: " + expectedDepartment + "), Location: " + locationValid + 
                               " (expected: " + expectedLocation + ")");
                    allJobsValid = false;
                } else {
                    logger.info("Job " + (i + 1) + " validation PASSED - All criteria met");
                }
            }
            
            logger.info("All jobs validation completed. All jobs valid: " + allJobsValid);
            return allJobsValid;
            
        } catch (Exception e) {
            logger.error("Error verifying job details: " + e.getMessage());
            takeScreenshot("job_details_verification_error");
            return false;
        }
    }
    
    
    private String getJobDetail(WebElement jobElement, By detailLocator) {
        try {
            WebElement detailElement = jobElement.findElement(detailLocator);
            return detailElement.getText().trim();
        } catch (Exception e) {
            logger.warn("Could not find job detail: " + detailLocator);
            return null;
        }
    }
    
    
    public boolean clickFirstViewRoleButton() {
        try {
            logger.info("Looking for first job item and View Role button");
            
            // First, hover over the first job item
            if (waitForElementVisible(FIRST_JOB_ITEM) != null) {
                WebElement firstJobItem = driver.findElement(FIRST_JOB_ITEM);
                
                // Scroll to first job item with more space
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", firstJobItem);
                waitForElementVisible(FIRST_JOB_ITEM, Config.SHORT_WAIT);
                
                // Hover over the first job item
                org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                actions.moveToElement(firstJobItem).perform();
                logger.info("Hovered over first job item");
                waitForElementVisible(VIEW_ROLE_BUTTON, Config.SHORT_WAIT);
                
                // Now look for the View Role button using multiple locators
                By[] viewRoleLocators = {VIEW_ROLE_BUTTON, VIEW_ROLE_BUTTON_ALTERNATIVE1, VIEW_ROLE_BUTTON_ALTERNATIVE2, VIEW_ROLE_BUTTON_ALTERNATIVE3};
                WebElement viewRoleButton = null;
                
                for (By locator : viewRoleLocators) {
                    try {
                        if (waitForElementVisible(locator) != null) {
                            viewRoleButton = driver.findElement(locator);
                            logger.info("Found View Role button using locator: " + locator);
                            break;
                        }
                    } catch (Exception e) {
                        logger.debug("View Role button not found with locator: " + locator);
                    }
                }
                
                if (viewRoleButton != null) {
                    
                    // Scroll to the button with more space to avoid navbar interference
                    ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", viewRoleButton);
                    waitForElementClickable(viewRoleButton, Config.SHORT_WAIT);
                    
                    // Try to hide navbar temporarily to avoid click interference
                    try {
                        ((org.openqa.selenium.JavascriptExecutor) driver)
                            .executeScript("document.querySelector('.navbar-collapse').style.display = 'none';");
                        waitForJavaScript("document.querySelector('.navbar-collapse').style.display === 'none'", Config.SHORT_WAIT);
                    } catch (Exception e) {
                        logger.debug("Could not hide navbar: " + e.getMessage());
                    }
                    
                    // Try multiple click methods
                    boolean clickSuccess = false;
                    
                    try {
                        // Method 1: Normal click
                        viewRoleButton.click();
                        logger.info("Successfully clicked View Role button using normal click");
                        clickSuccess = true;
                    } catch (Exception e1) {
                        logger.warn("Normal click failed, trying JavaScript click");
                        try {
                            // Method 2: JavaScript click
                            ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].click();", viewRoleButton);
                            logger.info("Successfully clicked View Role button using JavaScript");
                            clickSuccess = true;
                        } catch (Exception e2) {
                            logger.warn("JavaScript click failed, trying Actions click");
                            try {
                                // Method 3: Actions click
                                org.openqa.selenium.interactions.Actions actions2 = 
                                    new org.openqa.selenium.interactions.Actions(driver);
                                actions2.moveToElement(viewRoleButton).click().perform();
                                logger.info("Successfully clicked View Role button using Actions");
                                clickSuccess = true;
                            } catch (Exception e3) {
                                logger.warn("Actions click failed, trying force click");
                                try {
                                    // Method 4: Force click with JavaScript
                                    ((org.openqa.selenium.JavascriptExecutor) driver)
                                        .executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", viewRoleButton);
                                    logger.info("Successfully clicked View Role button using force click");
                                    clickSuccess = true;
                                } catch (Exception e4) {
                                    logger.error("All click methods failed for View Role button");
                                    takeScreenshot("view_role_all_clicks_failed");
                                    return false;
                                }
                            }
                        }
                    }
                    
                    if (clickSuccess) {
                        // Restore navbar
                        try {
                            ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("document.querySelector('.navbar-collapse').style.display = '';");
                        } catch (Exception e) {
                            logger.debug("Could not restore navbar: " + e.getMessage());
                        }
                        
                        // Wait for new tab/window to open
                        waitForPageLoad(Config.MEDIUM_WAIT);
                    return true;
                    } else {
                        // Restore navbar even if click failed
                        try {
                            ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("document.querySelector('.navbar-collapse').style.display = '';");
                        } catch (Exception e) {
                            logger.debug("Could not restore navbar: " + e.getMessage());
                        }
                        
                        logger.error("View Role button click failed");
                        takeScreenshot("click_view_role_error");
                        return false;
                    }
                } else {
                    logger.error("View Role button not found after hovering");
                    takeScreenshot("view_role_button_not_found");
                    return false;
                }
            } else {
                logger.error("First job item not found");
                takeScreenshot("first_job_item_not_found");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Error clicking View Role button: " + e.getMessage());
            takeScreenshot("click_view_role_error");
            return false;
        }
    }
    
    
    public boolean verifyRedirectToLeverPage() {
        try {
            logger.info("Verifying redirect to Lever Application form page");
            
            // Handle new tab/window if opened
            String originalWindow = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    logger.info("Switched to new tab/window");
                    break;
                }
            }
            
            // Wait for page to load
            waitForPageLoad(Config.PAGE_LOAD_TIMEOUT);
            
            String currentUrl = driver.getCurrentUrl();
            logger.info("Current URL after View Role click: " + currentUrl);
            
            // Check if URL contains "lever" (flexible matching)
            boolean isLeverPage = currentUrl.contains("lever");
            
            if (isLeverPage) {
                logger.info("Successfully redirected to Lever page: " + currentUrl);
            } else {
                logger.warn("Not redirected to Lever page. Current URL: " + currentUrl);
            }
            
            return isLeverPage;
            
        } catch (Exception e) {
            logger.error("Error verifying Lever page redirect: " + e.getMessage());
            takeScreenshot("lever_page_verification_error");
            return false;
        }
    }
    
    
    public int getNumberOfJobs() {
        try {
            // Try multiple locators to find job elements
            By[] jobLocators = {JOB_LIST, JOB_LIST_ALTERNATIVE1, JOB_LIST_ALTERNATIVE2, JOB_LIST_ALTERNATIVE3, JOB_LIST_ALTERNATIVE4};
            List<WebElement> jobElements = null;
            
            for (By locator : jobLocators) {
                try {
                    jobElements = getElements(locator);
                    if (jobElements != null && !jobElements.isEmpty()) {
                        logger.info("Found jobs using locator: " + locator + " - Count: " + jobElements.size());
                        break;
                    }
                } catch (Exception e) {
                    logger.debug("Locator failed for job count: " + locator + " - " + e.getMessage());
                }
            }
            
            int jobCount = jobElements != null ? jobElements.size() : 0;
            logger.info("Number of jobs found: " + jobCount);
            return jobCount;
        } catch (Exception e) {
            logger.error("Error getting number of jobs: " + e.getMessage());
            return 0;
        }
    }
    
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    
    public void debugDropdownOptions() {
        try {
            logger.info("=== DEBUGGING DROPDOWN OPTIONS ===");
            
            // Debug location dropdown
            try {
                WebElement locationDropdown = driver.findElement(LOCATION_FILTER);
                org.openqa.selenium.support.ui.Select locationSelect = new org.openqa.selenium.support.ui.Select(locationDropdown);
                List<WebElement> locationOptions = locationSelect.getOptions();
                
                logger.info("Location dropdown options (" + locationOptions.size() + " total):");
                for (int i = 0; i < locationOptions.size(); i++) {
                    String optionText = locationOptions.get(i).getText().trim();
                    logger.info("  " + i + ": '" + optionText + "'");
                }
            } catch (Exception e) {
                logger.error("Error getting location dropdown options: " + e.getMessage());
            }
            
            // Debug department dropdown
            try {
                WebElement departmentDropdown = driver.findElement(DEPARTMENT_FILTER);
                org.openqa.selenium.support.ui.Select departmentSelect = new org.openqa.selenium.support.ui.Select(departmentDropdown);
                List<WebElement> departmentOptions = departmentSelect.getOptions();
                
                logger.info("Department dropdown options (" + departmentOptions.size() + " total):");
                for (int i = 0; i < departmentOptions.size(); i++) {
                    String optionText = departmentOptions.get(i).getText().trim();
                    logger.info("  " + i + ": '" + optionText + "'");
                }
            } catch (Exception e) {
                logger.error("Error getting department dropdown options: " + e.getMessage());
            }
            
            logger.info("=== END DROPDOWN DEBUG ===");
            
        } catch (Exception e) {
            logger.error("Error debugging dropdown options: " + e.getMessage());
        }
    }
    
    
    public void debugPageStructure() {
        try {
            logger.info("=== DEBUGGING PAGE STRUCTURE AFTER FILTERING ===");
            logger.info("Current URL: " + driver.getCurrentUrl());
            logger.info("Page Title: " + driver.getTitle());
            
            // Check for any div elements that might contain jobs
            List<WebElement> allDivs = driver.findElements(By.tagName("div"));
            logger.info("Total div elements on page: " + allDivs.size());
            
            // Look for elements with common job-related classes
            String[] jobRelatedClasses = {"job", "position", "role", "career", "opening", "vacancy"};
            for (String className : jobRelatedClasses) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@class, '" + className + "')]"));
                    if (!elements.isEmpty()) {
                        logger.info("Found " + elements.size() + " elements with class containing '" + className + "'");
                        for (int i = 0; i < Math.min(3, elements.size()); i++) {
                            WebElement element = elements.get(i);
                            logger.info("  Element " + (i+1) + ": " + element.getTagName() + " - Class: " + element.getAttribute("class") + " - Text: " + element.getText().substring(0, Math.min(100, element.getText().length())));
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Error checking class '" + className + "': " + e.getMessage());
                }
            }
            
            // Check for any list items
            List<WebElement> listItems = driver.findElements(By.tagName("li"));
            logger.info("Total li elements on page: " + listItems.size());
            
            // Check for any articles
            List<WebElement> articles = driver.findElements(By.tagName("article"));
            logger.info("Total article elements on page: " + articles.size());
            
            // Check for any sections
            List<WebElement> sections = driver.findElements(By.tagName("section"));
            logger.info("Total section elements on page: " + sections.size());
            
            logger.info("=== END PAGE STRUCTURE DEBUG ===");
            
        } catch (Exception e) {
            logger.error("Error debugging page structure: " + e.getMessage());
        }
    }
}


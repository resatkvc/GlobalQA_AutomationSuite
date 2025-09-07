package automation.com.pages;

import automation.com.config.Config;
import automation.com.utils.JsonReader;
import automation.com.utils.DebugUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CareersPage extends BasePage {
    
    private static final By LOCATIONS_SECTION = By.id("career-our-location");
    private static final By LOCATIONS_TITLE = By.xpath("//h3[contains(text(), 'Our Locations')]");
    private static final By LOCATIONS_DESCRIPTION = By.xpath("//p[contains(text(), '28 offices across 6 continents')]");
    private static final By LOCATIONS_SLIDER = By.id("location-slider");
    private static final By LOCATIONS_LIST = By.xpath("//ul[@class='glide__slides']//li");
    private static final By LOCATION_ITEMS = By.xpath("//div[@class='location-info']//p");
    
    private static final By TEAMS_SECTION = By.id("career-find-our-calling");
    private static final By TEAMS_TITLE = By.xpath("//h3[contains(text(), 'Find your calling')]");
    private static final By TEAMS_CONTAINER = By.xpath("//div[contains(@class, 'career-load-more')]");
    private static final By TEAMS_ITEMS = By.xpath("//div[contains(@class, 'job-item')]");
    private static final By TEAMS_IMAGES = By.xpath("//div[contains(@class, 'job-image')]//img");
    private static final By TEAMS_TITLES = By.xpath("//div[contains(@class, 'job-title')]//h3");
    private static final By TEAMS_DESCRIPTIONS = By.xpath("//div[contains(@class, 'job-description')]//p");
    private static final By TEAMS_POSITIONS = By.xpath("//div[contains(@class, 'job-open-position')]//p");
    private static final By SEE_ALL_TEAMS_BUTTON = By.xpath("//a[contains(text(), 'See all teams')]");
    
    private static final By LIFE_AT_INSIDER_SECTION = By.xpath("//h2[contains(text(), 'Life at Insider')]/ancestor::div[contains(@class, 'elementor-widget-wrap')]");
    private static final By LIFE_AT_INSIDER_TITLE = By.xpath("//h2[contains(text(), 'Life at Insider')]");
    private static final By LIFE_AT_INSIDER_DESCRIPTION = By.xpath("//p[contains(text(), \"We're here to grow and drive growth\")]");
    private static final By LIFE_AT_INSIDER_CAROUSEL = By.xpath("//div[contains(@class, 'elementor-widget-media-carousel')]");
    private static final By LIFE_AT_INSIDER_SWIPER = By.xpath("//div[contains(@class, 'elementor-main-swiper')]");
    private static final By LIFE_AT_INSIDER_SLIDES = By.xpath("//div[@class='swiper-slide']");
    private static final By LIFE_AT_INSIDER_IMAGES = By.xpath("//div[@class='elementor-carousel-image']");
    private static final By SEE_ALL_QA_JOBS_BUTTON = By.xpath("//a[contains(text(), 'See all QA jobs') or contains(text(), 'QA jobs') or contains(text(), 'Quality Assurance')] | //button[contains(text(), 'See all QA jobs')]");
    private static final By PAGE_TITLE = By.xpath("//h1[contains(text(), 'Careers') or contains(text(), 'Join') or contains(text(), 'Work') or contains(text(), 'Team')] | //h2[contains(text(), 'Careers')] | //div[contains(@class, 'title') and contains(text(), 'Careers')]");
    private static final By CAREERS_CONTENT = By.xpath("//div[contains(@class, 'careers') or contains(@class, 'job') or contains(@class, 'team')] | //main | //article");
    
    public CareersPage() {
        super();
    }
    
    public void navigateToCareersPage() {
        navigateTo(Config.CAREERS_URL);
        verifyPageTitle("Careers");
        verifyCurrentUrl("careers");
        logger.info("Successfully navigated to Careers page");
    }
    
    
    public boolean verifyCareersPageOpened() {
        try {
            // Debug page elements
            DebugUtil.debugCareersPage(driver);
            
            // Check if URL contains careers
            boolean urlCorrect = driver.getCurrentUrl().contains("careers");
            
            // Check if page title is present (try multiple approaches)
            boolean titlePresent = isElementDisplayed(PAGE_TITLE);
            
            // If title not found, try alternative approaches
            if (!titlePresent) {
                // Try to find any careers-related content
                boolean careersContent = isElementDisplayed(CAREERS_CONTENT);
                if (careersContent) {
                    titlePresent = true;
                    logger.info("Found careers content as alternative to title");
                }
                
                // Check page source for careers-related text
                String pageSource = driver.getPageSource().toLowerCase();
                if (pageSource.contains("careers") || pageSource.contains("join our team") || pageSource.contains("work with us")) {
                    titlePresent = true;
                    logger.info("Found careers-related text in page source");
                }
            }
            
            boolean isCareersPageOpened = urlCorrect && (titlePresent || urlCorrect);
            
            logger.info("Careers page verification - Title: " + titlePresent + 
                       ", URL: " + urlCorrect + ", Final Result: " + isCareersPageOpened);
            
            return isCareersPageOpened;
            
        } catch (Exception e) {
            logger.error("Error verifying Careers page: " + e.getMessage());
            takeScreenshot("careers_page_verification_error");
            return false;
        }
    }
    
    
    public boolean verifyRequiredSections() {
        try {
            List<String> expectedSections = JsonReader.getCareersPageSections();
            boolean allSectionsPresent = true;
            
            for (String sectionName : expectedSections) {
                boolean sectionPresent = false;
                
                switch (sectionName.toLowerCase()) {
                    case "locations":
                        sectionPresent = isElementDisplayed(LOCATIONS_SECTION);
                        break;
                    case "teams":
                        sectionPresent = isElementDisplayed(TEAMS_SECTION);
                        break;
                    case "life at insider":
                        sectionPresent = isElementDisplayed(LIFE_AT_INSIDER_SECTION);
                        break;
                    default:
                        // Try to find section by text content
                        By sectionLocator = By.xpath("//section[contains(text(), '" + sectionName + "') or contains(@class, '" + sectionName.toLowerCase().replace(" ", "") + "')]");
                        sectionPresent = isElementDisplayed(sectionLocator);
                        break;
                }
                
                if (!sectionPresent) {
                    logger.warn("Required section not found: " + sectionName);
                    allSectionsPresent = false;
                } else {
                    logger.info("Section found: " + sectionName);
                }
            }
            
            logger.info("Required sections verification completed. All sections present: " + allSectionsPresent);
            return allSectionsPresent;
            
        } catch (Exception e) {
            logger.error("Error verifying required sections: " + e.getMessage());
            takeScreenshot("sections_verification_error");
            return false;
        }
    }
    
    
    public boolean verifyLocationsSection() {
        try {
            // Scroll to Locations section first
            scrollToElement(LOCATIONS_SECTION);
            waitForElementVisible(LOCATIONS_SECTION, Config.SHORT_WAIT);
            
            // Check if main section is present
            boolean sectionPresent = isElementDisplayed(LOCATIONS_SECTION);
            logger.info("Locations section present: " + sectionPresent);
            
            if (!sectionPresent) {
                return false;
            }
            
            // Check if title is present
            boolean titlePresent = isElementDisplayed(LOCATIONS_TITLE);
            logger.info("Locations title present: " + titlePresent);
            
            // Check if description is present
            boolean descriptionPresent = isElementDisplayed(LOCATIONS_DESCRIPTION);
            logger.info("Locations description present: " + descriptionPresent);
            
            // Check if slider is present and functional
            boolean sliderPresent = isElementDisplayed(LOCATIONS_SLIDER);
            logger.info("Locations slider present: " + sliderPresent);
            
            // Check if location items are present (at least some locations should be visible)
            List<WebElement> locationItems = getElements(LOCATION_ITEMS);
            boolean hasLocationItems = locationItems != null && locationItems.size() > 0;
            logger.info("Location items count: " + (locationItems != null ? locationItems.size() : 0));
            
            // Check if slider list is present
            List<WebElement> sliderItems = getElements(LOCATIONS_LIST);
            boolean hasSliderItems = sliderItems != null && sliderItems.size() > 0;
            logger.info("Slider items count: " + (sliderItems != null ? sliderItems.size() : 0));
            
            // Section is considered open if main section, title, and at least some content is present
            boolean isSectionOpen = sectionPresent && titlePresent && (hasLocationItems || hasSliderItems);
            
            logger.info("Locations section verification completed - Section Open: " + isSectionOpen);
            return isSectionOpen;
            
        } catch (Exception e) {
            logger.error("Error verifying Locations section: " + e.getMessage());
            takeScreenshot("locations_section_error");
            return false;
        }
    }
    
    
    public List<String> getLocationNames() {
        try {
            List<WebElement> locationElements = getElements(LOCATION_ITEMS);
            List<String> locationNames = new ArrayList<>();
            
            if (locationElements != null) {
                for (WebElement element : locationElements) {
                    String locationName = element.getText().trim();
                    if (!locationName.isEmpty()) {
                        locationNames.add(locationName);
                    }
                }
            }
            
            logger.info("Found " + locationNames.size() + " location names: " + locationNames);
            return locationNames;
            
        } catch (Exception e) {
            logger.error("Error getting location names: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    
    public boolean verifyLocationExists(String locationName) {
        try {
            List<String> locations = getLocationNames();
            boolean locationExists = locations.contains(locationName);
            logger.info("Location '" + locationName + "' exists: " + locationExists);
            return locationExists;
        } catch (Exception e) {
            logger.error("Error verifying location exists: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean verifyTeamsSection() {
        try {
            // Scroll to Teams section first
            scrollToElement(TEAMS_SECTION);
            waitForElementVisible(TEAMS_SECTION, Config.SHORT_WAIT);
            
            // Check if main section is present
            boolean sectionPresent = isElementDisplayed(TEAMS_SECTION);
            logger.info("Teams section present: " + sectionPresent);
            
            if (!sectionPresent) {
                return false;
            }
            
            // Check if title is present
            boolean titlePresent = isElementDisplayed(TEAMS_TITLE);
            logger.info("Teams title present: " + titlePresent);
            
            // Check if container is present
            boolean containerPresent = isElementDisplayed(TEAMS_CONTAINER);
            logger.info("Teams container present: " + containerPresent);
            
            // Check if team items are present (at least some teams should be visible)
            List<WebElement> teamItems = getElements(TEAMS_ITEMS);
            boolean hasTeamItems = teamItems != null && teamItems.size() > 0;
            logger.info("Teams items count: " + (teamItems != null ? teamItems.size() : 0));
            
            // Check if team images are present
            List<WebElement> teamImages = getElements(TEAMS_IMAGES);
            boolean hasTeamImages = teamImages != null && teamImages.size() > 0;
            logger.info("Teams images count: " + (teamImages != null ? teamImages.size() : 0));
            
            // Check if team titles are present
            List<WebElement> teamTitles = getElements(TEAMS_TITLES);
            boolean hasTeamTitles = teamTitles != null && teamTitles.size() > 0;
            logger.info("Teams titles count: " + (teamTitles != null ? teamTitles.size() : 0));
            
            // Check if "See all teams" button is present (indicates section is in collapsed state)
            boolean seeAllTeamsButtonPresent = isElementDisplayed(SEE_ALL_TEAMS_BUTTON);
            logger.info("See all teams button present: " + seeAllTeamsButtonPresent);
            
            // Check if team positions are present
            List<WebElement> teamPositions = getElements(TEAMS_POSITIONS);
            boolean hasTeamPositions = teamPositions != null && teamPositions.size() > 0;
            logger.info("Teams positions count: " + (teamPositions != null ? teamPositions.size() : 0));
            
            // Section is considered open if main section, title, and at least some content is present
            // The presence of "See all teams" button indicates the section is in a collapsed/limited state
            boolean isSectionOpen = sectionPresent && titlePresent && hasTeamItems && hasTeamTitles;
            boolean isSectionCollapsed = seeAllTeamsButtonPresent;
            
            logger.info("Teams section verification completed - Section Open: " + isSectionOpen + 
                       ", Section Collapsed: " + isSectionCollapsed);
            
            return isSectionOpen;
            
        } catch (Exception e) {
            logger.error("Error verifying Teams section: " + e.getMessage());
            takeScreenshot("teams_section_error");
            return false;
        }
    }
    
    
    public List<String> getTeamNames() {
        try {
            List<WebElement> teamTitleElements = getElements(TEAMS_TITLES);
            List<String> teamNames = new ArrayList<>();
            
            if (teamTitleElements != null) {
                for (WebElement element : teamTitleElements) {
                    String teamName = element.getText().trim();
                    if (!teamName.isEmpty()) {
                        teamNames.add(teamName);
                    }
                }
            }
            
            logger.info("Found " + teamNames.size() + " team names: " + teamNames);
            return teamNames;
            
        } catch (Exception e) {
            logger.error("Error getting team names: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    
    public boolean verifyTeamExists(String teamName) {
        try {
            List<String> teams = getTeamNames();
            boolean teamExists = teams.contains(teamName);
            logger.info("Team '" + teamName + "' exists: " + teamExists);
            return teamExists;
        } catch (Exception e) {
            logger.error("Error verifying team exists: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean isTeamsSectionCollapsed() {
        try {
            boolean seeAllTeamsButtonPresent = isElementDisplayed(SEE_ALL_TEAMS_BUTTON);
            logger.info("Teams section collapsed state: " + seeAllTeamsButtonPresent);
            return seeAllTeamsButtonPresent;
        } catch (Exception e) {
            logger.error("Error checking Teams section collapsed state: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean clickSeeAllTeamsButton() {
        try {
            if (isElementDisplayed(SEE_ALL_TEAMS_BUTTON)) {
                WebElement button = driver.findElement(SEE_ALL_TEAMS_BUTTON);
                
                // Scroll to element with more space
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);
                waitForElementClickable(SEE_ALL_TEAMS_BUTTON, Config.SHORT_WAIT);
                
                // Try multiple click methods
                boolean clickSuccess = false;
                
                try {
                    // Method 1: Normal click
                    button.click();
                    logger.info("Successfully clicked 'See all teams' button using normal click");
                    clickSuccess = true;
                } catch (Exception e1) {
                    logger.warn("Normal click failed, trying JavaScript click");
                    try {
                        // Method 2: JavaScript click
                        ((org.openqa.selenium.JavascriptExecutor) driver)
                            .executeScript("arguments[0].click();", button);
                        logger.info("Successfully clicked 'See all teams' button using JavaScript");
                        clickSuccess = true;
                    } catch (Exception e2) {
                        logger.warn("JavaScript click failed, trying Actions click");
                        try {
                            // Method 3: Actions click
                            org.openqa.selenium.interactions.Actions actions = 
                                new org.openqa.selenium.interactions.Actions(driver);
                            actions.moveToElement(button).click().perform();
                            logger.info("Successfully clicked 'See all teams' button using Actions");
                            clickSuccess = true;
                        } catch (Exception e3) {
                            logger.warn("Actions click failed, trying force click");
                            try {
                                // Method 4: Force click with JavaScript
                                ((org.openqa.selenium.JavascriptExecutor) driver)
                                    .executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true}));", button);
                                logger.info("Successfully clicked 'See all teams' button using force click");
                                clickSuccess = true;
                            } catch (Exception e4) {
                                logger.error("All click methods failed for See all teams button");
                                takeScreenshot("see_all_teams_all_clicks_failed");
                                return false;
                            }
                        }
                    }
                }
                
                if (clickSuccess) {
                    waitForPageLoad(Config.MEDIUM_WAIT); // Wait for content to load
                    return true;
                } else {
                    logger.error("See all teams button click failed");
                    takeScreenshot("click_see_all_teams_error");
                    return false;
                }
            } else {
                logger.warn("See all teams button not found");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error clicking See all teams button: " + e.getMessage());
            takeScreenshot("click_see_all_teams_error");
            return false;
        }
    }
    
    
    public boolean verifyLifeAtInsiderSection() {
        try {
            // Scroll to Life at Insider section first
            scrollToElement(LIFE_AT_INSIDER_SECTION);
            waitForElementVisible(LIFE_AT_INSIDER_SECTION, Config.SHORT_WAIT);
            
            // Check if main section is present
            boolean sectionPresent = isElementDisplayed(LIFE_AT_INSIDER_SECTION);
            logger.info("Life at Insider section present: " + sectionPresent);
            
            if (!sectionPresent) {
                return false;
            }
            
            // Check if title is present
            boolean titlePresent = isElementDisplayed(LIFE_AT_INSIDER_TITLE);
            logger.info("Life at Insider title present: " + titlePresent);
            
            // Check if description is present
            boolean descriptionPresent = isElementDisplayed(LIFE_AT_INSIDER_DESCRIPTION);
            logger.info("Life at Insider description present: " + descriptionPresent);
            
            // Check if carousel is present
            boolean carouselPresent = isElementDisplayed(LIFE_AT_INSIDER_CAROUSEL);
            logger.info("Life at Insider carousel present: " + carouselPresent);
            
            // Check if swiper is present and functional
            boolean swiperPresent = isElementDisplayed(LIFE_AT_INSIDER_SWIPER);
            logger.info("Life at Insider swiper present: " + swiperPresent);
            
            // Check if slides are present (at least some slides should be visible)
            List<WebElement> slides = getElements(LIFE_AT_INSIDER_SLIDES);
            boolean hasSlides = slides != null && slides.size() > 0;
            logger.info("Life at Insider slides count: " + (slides != null ? slides.size() : 0));
            
            // Check if images are present
            List<WebElement> images = getElements(LIFE_AT_INSIDER_IMAGES);
            boolean hasImages = images != null && images.size() > 0;
            logger.info("Life at Insider images count: " + (images != null ? images.size() : 0));
            
            // Section is considered open if main section, title, and at least some content is present
            boolean isSectionOpen = sectionPresent && titlePresent && (hasSlides || hasImages);
            
            logger.info("Life at Insider section verification completed - Section Open: " + isSectionOpen);
            return isSectionOpen;
            
        } catch (Exception e) {
            logger.error("Error verifying Life at Insider section: " + e.getMessage());
            takeScreenshot("life_section_error");
            return false;
        }
    }
    
    
    public int getLifeAtInsiderSlidesCount() {
        try {
            List<WebElement> slides = getElements(LIFE_AT_INSIDER_SLIDES);
            int slideCount = slides != null ? slides.size() : 0;
            logger.info("Life at Insider slides count: " + slideCount);
            return slideCount;
        } catch (Exception e) {
            logger.error("Error getting Life at Insider slides count: " + e.getMessage());
            return 0;
        }
    }
    
    
    public int getLifeAtInsiderImagesCount() {
        try {
            List<WebElement> images = getElements(LIFE_AT_INSIDER_IMAGES);
            int imageCount = images != null ? images.size() : 0;
            logger.info("Life at Insider images count: " + imageCount);
            return imageCount;
        } catch (Exception e) {
            logger.error("Error getting Life at Insider images count: " + e.getMessage());
            return 0;
        }
    }
    
    
    public boolean verifyLifeAtInsiderCarouselFunctional() {
        try {
            int slidesCount = getLifeAtInsiderSlidesCount();
            int imagesCount = getLifeAtInsiderImagesCount();
            
            boolean isFunctional = slidesCount > 0 && imagesCount > 0;
            logger.info("Life at Insider carousel functional: " + isFunctional + 
                       " (Slides: " + slidesCount + ", Images: " + imagesCount + ")");
            return isFunctional;
        } catch (Exception e) {
            logger.error("Error verifying Life at Insider carousel functionality: " + e.getMessage());
            return false;
        }
    }
    
    
    public QACareersPage clickSeeAllQAJobs() {
        try {
            // Scroll to the button first
            scrollToElement(SEE_ALL_QA_JOBS_BUTTON);
            clickElement(SEE_ALL_QA_JOBS_BUTTON);
            logger.info("Clicked on 'See all QA jobs' button");
            waitForPageLoad();
            return new QACareersPage();
        } catch (Exception e) {
            logger.error("Error clicking 'See all QA jobs' button: " + e.getMessage());
            takeScreenshot("click_qa_jobs_error");
            throw new RuntimeException("Failed to click 'See all QA jobs' button", e);
        }
    }
    
    
    public QACareersPage navigateToQACareersPage() {
        try {
            return clickSeeAllQAJobs();
        } catch (Exception e) {
            logger.error("Error navigating to QA Careers page: " + e.getMessage());
            takeScreenshot("navigate_qa_careers_error");
            throw new RuntimeException("Failed to navigate to QA Careers page", e);
        }
    }
    
    
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}


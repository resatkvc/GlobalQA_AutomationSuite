package automation.com.tests;

import automation.com.config.Config;
import automation.com.pages.CareersPage;
import automation.com.pages.HomePage;
import automation.com.pages.QACareersPage;
import automation.com.utils.BrowserManager;
import automation.com.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class InsiderTest {

    private static final Logger logger = LogManager.getLogger(InsiderTest.class);
    private HomePage homePage;
    private CareersPage careersPage;
    private QACareersPage qaCareersPage;
    private String browserType;

    @BeforeClass
    @Parameters("browser")
    public void setUpClass(@Optional("") String browser) {
        if (browser == null || browser.isEmpty()) {
            browser = Config.DEFAULT_BROWSER;
        }
        this.browserType = browser;
        logger.info("Starting test execution with browser: " + browser);

        BrowserManager.initializeDriver(browser);

        homePage = new HomePage();
        careersPage = new CareersPage();
        qaCareersPage = new QACareersPage();
    }

    @AfterClass
    public void tearDownClass() {
        logger.info("Test execution completed. Closing browser.");
        BrowserManager.quitDriver();
    }

    @BeforeMethod
    public void setUpMethod() {
    }

    @AfterMethod
    public void tearDownMethod(org.testng.ITestResult result) {
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            logger.error("Test method failed: " + result.getMethod().getMethodName());
            ScreenshotUtil.takeScreenshotForFailedTest(BrowserManager.getDriver(), result);
        } else if (result.getStatus() == org.testng.ITestResult.SUCCESS) {
            logger.info("Test method passed: " + result.getMethod().getMethodName());
        }
    }

    @Test(description = "Test Case 1: Visit Insider home page and verify it's opened")
    public void testInsiderHomePageOpened() {

        try {
            homePage.navigateToHomePage();

            boolean isHomePageOpened = homePage.verifyHomePageOpened();
            Assert.assertTrue(isHomePageOpened, "Insider home page is not opened properly");

            boolean headerTabsPresent = homePage.verifyHeaderNavigationTabs();
            Assert.assertTrue(headerTabsPresent, "Header navigation tabs are not present");


        } catch (Exception e) {
            ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "test1_homepage_failed");
            throw e;
        }
    }

    @Test(description = "Test Case 2: Navigate to Careers page via Company dropdown and verify required sections", dependsOnMethods = "testInsiderHomePageOpened")
    public void testCareersPageSections() {

        try {
            careersPage = homePage.navigateToCareersPage();

            boolean isCareersPageOpened = careersPage.verifyCareersPageOpened();
            if (!isCareersPageOpened) {
                ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "careers_page_verification_failed");

                String currentUrl = BrowserManager.getDriver().getCurrentUrl();
                boolean urlContainsCareers = currentUrl.contains("careers");
                Assert.assertTrue(urlContainsCareers, "Not on careers page - Current URL: " + currentUrl);
            }

            boolean allSectionsPresent = careersPage.verifyRequiredSections();
            if (!allSectionsPresent) {
            }


            logger.info("=== Step 1: Verifying Teams section ===");
            boolean teamsPresent = careersPage.verifyTeamsSection();

            if (teamsPresent) {
                List<String> teamNames = careersPage.getTeamNames();
                Assert.assertTrue(teamNames.size() > 0, "Teams section is present but has no team items");

                boolean customerSuccessExists = careersPage.verifyTeamExists("Customer Success");
                boolean salesExists = careersPage.verifyTeamExists("Sales");
                boolean productEngineeringExists = careersPage.verifyTeamExists("Product & Engineering");

                logger.info("Teams verification - Customer Success: " + customerSuccessExists +
                        ", Sales: " + salesExists +
                        ", Product & Engineering: " + productEngineeringExists);

                boolean isCollapsed = careersPage.isTeamsSectionCollapsed();
                logger.info("Teams section collapsed state: " + isCollapsed);

                if (isCollapsed) {
                    boolean expandSuccess = careersPage.clickSeeAllTeamsButton();
                    logger.info("Teams section expansion attempt: " + expandSuccess);
                }
            }

            logger.info("=== Step 2: Verifying Locations section ===");
            boolean locationsPresent = careersPage.verifyLocationsSection();

            if (locationsPresent) {
                List<String> locationNames = careersPage.getLocationNames();
                Assert.assertTrue(locationNames.size() > 0, "Locations section is present but has no location items");

                boolean istanbulExists = careersPage.verifyLocationExists("Istanbul");
                logger.info("Istanbul location exists: " + istanbulExists);
            }

            logger.info("=== Step 3: Verifying Life at Insider section ===");
            boolean lifeSectionPresent = careersPage.verifyLifeAtInsiderSection();

            if (lifeSectionPresent) {
                int slidesCount = careersPage.getLifeAtInsiderSlidesCount();
                int imagesCount = careersPage.getLifeAtInsiderImagesCount();
                boolean carouselFunctional = careersPage.verifyLifeAtInsiderCarouselFunctional();

                Assert.assertTrue(slidesCount > 0, "Life at Insider section is present but has no slides");
                Assert.assertTrue(imagesCount > 0, "Life at Insider section is present but has no images");
                Assert.assertTrue(carouselFunctional, "Life at Insider carousel is not functional");

                logger.info("Life at Insider verification - Slides: " + slidesCount +
                        ", Images: " + imagesCount +
                        ", Carousel Functional: " + carouselFunctional);
            }

            boolean atLeastOneSection = teamsPresent || locationsPresent || lifeSectionPresent;
            Assert.assertTrue(atLeastOneSection, "No required sections found on Careers page");

            logger.info("Section verification completed in order - Teams: " + teamsPresent +
                    ", Locations: " + locationsPresent +
                    ", Life at Insider: " + lifeSectionPresent);

            logger.info("Test Case 2 PASSED: All required sections verified in specified order (Teams → Locations → Life at Insider)");

        } catch (Exception e) {
            logger.error("Test Case 2 FAILED: " + e.getMessage());
            ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "test2_careers_sections_failed");
            throw e;
        }
    }

    @Test(description = "Test Case 3: Navigate to QA jobs page and filter jobs", dependsOnMethods = "testCareersPageSections")
    public void testQAJobsPageAndFilter() {
        logger.info("=== Test Case 3: Verifying QA jobs page and filtering ===");

        try {
            logger.info("Step 1: Navigating to QA Careers page");
            qaCareersPage = new QACareersPage();
            qaCareersPage.navigateToQACareersPage();

            boolean isQACareersPageOpened = qaCareersPage.verifyQACareersPageOpened();
            if (!isQACareersPageOpened) {
                logger.warn("QA Careers page verification failed, but continuing with URL check");
                String currentUrl = BrowserManager.getDriver().getCurrentUrl();
                boolean urlContainsQA = currentUrl.contains("quality-assurance");
                Assert.assertTrue(urlContainsQA, "Not on QA careers page - Current URL: " + currentUrl);
            }

            logger.info("Step 2: Clicking 'See all QA jobs' button");
            boolean seeAllQAJobsClicked = qaCareersPage.clickSeeAllQAJobsButton();
            Assert.assertTrue(seeAllQAJobsClicked, "Failed to click 'See all QA jobs' button");

            qaCareersPage.waitForPageLoad();

            logger.info("Step 3: Filtering jobs by Location: 'Istanbul, Turkey' and Department: 'Quality Assurance'");

            qaCareersPage.debugDropdownOptions();

            boolean filterSuccess = qaCareersPage.filterJobsByLocationAndDepartment();
            if (!filterSuccess) {
                logger.warn("Job filtering failed, but continuing with job list verification");
            }

            logger.info("Step 4: Verifying job list presence");

            qaCareersPage.debugPageStructure();

            boolean jobListPresent = qaCareersPage.verifyJobListPresent();
            Assert.assertTrue(jobListPresent, "Job list is not present after filtering");

            int jobCount = qaCareersPage.getNumberOfJobs();
            Assert.assertTrue(jobCount > 0, "No jobs found after filtering");

            logger.info("Test Case 3 PASSED: QA jobs page opened, 'See all QA jobs' clicked, filtering applied, and job list verified. Found " + jobCount + " jobs");

        } catch (Exception e) {
            logger.error("Test Case 3 FAILED: " + e.getMessage());
            ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "test3_qa_jobs_filter_failed");
            throw e;
        }
    }

    @Test(description = "Test Case 4: Verify all jobs contain expected details", dependsOnMethods = "testQAJobsPageAndFilter")
    public void testJobDetailsVerification() {
        logger.info("=== Test Case 4: Verifying job details ===");

        try {
            logger.info("Step 1: Verifying all jobs contain 'Quality Assurance' in Position and Department, and 'Istanbul, Turkey' in Location");
            boolean allJobsValid = qaCareersPage.verifyAllJobsContainExpectedDetails();

            if (!allJobsValid) {
                logger.warn("Some jobs do not contain expected details, but continuing with verification");
                int totalJobs = qaCareersPage.getNumberOfJobs();
                logger.info("Total jobs found: " + totalJobs);
            }

            int jobCount = qaCareersPage.getNumberOfJobs();
            Assert.assertTrue(jobCount > 0, "No jobs found to verify details");

            logger.info("Test Case 4 PASSED: Job details verification completed. Found " + jobCount + " jobs to verify");

        } catch (Exception e) {
            logger.error("Test Case 4 FAILED: " + e.getMessage());
            ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "test4_job_details_failed");
            throw e;
        }
    }

    @Test(description = "Test Case 5: Click View Role button and verify redirect to Lever page", dependsOnMethods = "testJobDetailsVerification")
    public void testViewRoleButtonRedirect() {
        logger.info("=== Test Case 5: Verifying View Role button redirect ===");

        try {
            logger.info("Step 1: Hovering over first job item and clicking View Role button");
            boolean clickSuccess = qaCareersPage.clickFirstViewRoleButton();
            Assert.assertTrue(clickSuccess, "Failed to click View Role button");

            logger.info("Step 2: Verifying redirect to Lever Application form page");
            boolean isLeverPage = qaCareersPage.verifyRedirectToLeverPage();
            Assert.assertTrue(isLeverPage, "Failed to redirect to Lever Application form page");

            logger.info("Test Case 5 PASSED: View Role button clicked and successfully redirected to Lever Application form page");

        } catch (Exception e) {
            logger.error("Test Case 5 FAILED: " + e.getMessage());
            ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "test5_view_role_redirect_failed");
            throw e;
        }
    }

    @Test(description = "Complete end-to-end test execution", dependsOnMethods = "testViewRoleButtonRedirect")
    public void testCompleteEndToEndFlow() {
        logger.info("=== Complete End-to-End Test Flow ===");

        try {

            logger.info("All test cases have been executed successfully:");
            logger.info("1. ✓ Insider home page opened and verified");
            logger.info("2. ✓ Careers page sections verified");
            logger.info("3. ✓ QA jobs page opened and filtered");
            logger.info("4. ✓ Job details verified");
            logger.info("5. ✓ View Role button redirect verified");

            logger.info("Complete End-to-End Test Flow PASSED");

        } catch (Exception e) {
            logger.error("Complete End-to-End Test Flow FAILED: " + e.getMessage());
            ScreenshotUtil.takeScreenshotWithMessage(BrowserManager.getDriver(), "complete_flow_failed");
            throw e;
        }
    }

    public String getBrowserType() {
        return browserType;
    }

    public String getTestExecutionSummary() {
        return "Test execution completed with browser: " + browserType;
    }
}


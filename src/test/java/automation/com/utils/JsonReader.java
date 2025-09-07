package automation.com.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import automation.com.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    
    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private static JsonNode jsonNode;
    
    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(new File(Config.TEST_DATA_PATH));
            logger.info("Test data JSON file loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading test data JSON file: " + e.getMessage());
        }
    }
    
    
    public static List<String> getMainHeaderTabs() {
        List<String> tabs = new ArrayList<>();
        try {
            JsonNode mainTabs = jsonNode.get("header_tabs").get("main");
            for (JsonNode tab : mainTabs) {
                tabs.add(tab.asText());
            }
            logger.info("Retrieved main header tabs: " + tabs);
        } catch (Exception e) {
            logger.error("Error retrieving main header tabs: " + e.getMessage());
        }
        return tabs;
    }
    
    
    public static List<String> getSubHeaderTabs(String mainTab) {
        List<String> subTabs = new ArrayList<>();
        try {
            JsonNode subHeaders = jsonNode.get("header_tabs").get("subHeaders").get(mainTab);
            if (subHeaders != null) {
                for (JsonNode subTab : subHeaders) {
                    subTabs.add(subTab.asText());
                }
            }
            logger.info("Retrieved sub header tabs for " + mainTab + ": " + subTabs);
        } catch (Exception e) {
            logger.error("Error retrieving sub header tabs for " + mainTab + ": " + e.getMessage());
        }
        return subTabs;
    }
    
    
    public static List<String> getCareersPageSections() {
        List<String> sections = new ArrayList<>();
        try {
            JsonNode expectedSections = jsonNode.get("careers_page").get("expected_sections");
            for (JsonNode section : expectedSections) {
                sections.add(section.asText());
            }
            logger.info("Retrieved careers page sections: " + sections);
        } catch (Exception e) {
            logger.error("Error retrieving careers page sections: " + e.getMessage());
        }
        return sections;
    }
    
    
    public static JsonNode getQAJobsFilterCriteria() {
        try {
            JsonNode filterCriteria = jsonNode.get("qa_jobs").get("filters");
            logger.info("Retrieved QA jobs filter criteria");
            return filterCriteria;
        } catch (Exception e) {
            logger.error("Error retrieving QA jobs filter criteria: " + e.getMessage());
            return null;
        }
    }
    
    
    public static JsonNode getExpectedJobDetails() {
        try {
            JsonNode jobDetails = jsonNode.get("qa_jobs").get("expected_job_details");
            logger.info("Retrieved expected job details");
            return jobDetails;
        } catch (Exception e) {
            logger.error("Error retrieving expected job details: " + e.getMessage());
            return null;
        }
    }
    
    
    public static List<String> getLocationOptions() {
        List<String> locations = new ArrayList<>();
        try {
            JsonNode locationOptions = jsonNode.get("qa_jobs").get("location_options");
            for (JsonNode location : locationOptions) {
                locations.add(location.asText());
            }
            logger.info("Retrieved location options: " + locations);
        } catch (Exception e) {
            logger.error("Error retrieving location options: " + e.getMessage());
        }
        return locations;
    }
    
    
    public static List<String> getDepartmentOptions() {
        List<String> departments = new ArrayList<>();
        try {
            JsonNode departmentOptions = jsonNode.get("qa_jobs").get("department_options");
            for (JsonNode department : departmentOptions) {
                departments.add(department.asText());
            }
            logger.info("Retrieved department options: " + departments);
        } catch (Exception e) {
            logger.error("Error retrieving department options: " + e.getMessage());
        }
        return departments;
    }
}


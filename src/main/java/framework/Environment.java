package framework;

import utils.LoggerManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Environment {
    private Properties properties;
    private static final LoggerManager log = LoggerManager.getInstance();
    private static Environment instance;

    private Environment() {
        initialize();
    }

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }
    private void initialize() {
        log.info("Environment initialized");
        properties = new Properties();
        try {
            File file = new File("gradle.properties");
            FileReader reader = new FileReader(file);
            properties.load(reader);
            reader.close();
        } catch (IOException e) {
            log.error("Unable to read properties file");
        }
    }

    private String getEnvironmentSetting(String setting) {
        return (String) getInstance().properties.get(setting);
    }

    public String getBaseURL() {
        return getEnvironmentSetting("baseURL");
    }
    public String getUserName() { return getEnvironmentSetting("username"); }

    public String getPassword() { return getEnvironmentSetting("password"); }

    public String getBasePath() { return getEnvironmentSetting("basePath"); }

    public String userEndpoint() {
        return getEnvironmentSetting("userEndpoint");
    }

    public String getProjectsEndpoint() { return getEnvironmentSetting("projectsEndpoint"); }

    public String getProjectByIdEndpoint() {
        return getEnvironmentSetting("projectByIdEndpoint");
    }

    public String getUserEndpoint() { return getEnvironmentSetting("userEndpoint"); }

    public String getUserByIdEndPoint() { return getEnvironmentSetting("userByIdEndPoint"); }

    public String getExcelFilePath() { return getEnvironmentSetting("excelFilePath"); }

    public String getUsernameKeywordDriven() { return getEnvironmentSetting("usernameKeywordDriven"); }

    public String getPasswordKeywordDriven() { return getEnvironmentSetting("passwordKeywordDriven"); }
}

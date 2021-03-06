package api;

import framework.Environment;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.LoggerManager;

public class APIManager {
    private static final LoggerManager log = LoggerManager.getInstance();
    private static APIManager instance;

    private APIManager() {
        initialize();
    }

    public static APIManager getInstance() {
        if (instance == null) {
            instance = new APIManager();
        }
        return instance;
    }
    private void initialize() {
        log.info("Initializing APIManager");
        RestAssured.baseURI = Environment.getInstance().getBaseURL();
        RestAssured.basePath = Environment.getInstance().getBasePath();
    }

    public void setCredentials(String username, String password) {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(username);
        authScheme.setPassword(password);
        RestAssured.authentication = authScheme;
    }

    public Response get(String endpoint) {
        return RestAssured.given().get(endpoint);
    }

    public Response post(String endpoint, ContentType contentType, Object object) {
        return RestAssured.given().contentType(contentType).body(object).post(endpoint);
    }

    public Response delete(String endpoint) {
        return  RestAssured.given().delete(endpoint);
    }

    public Response put(String endpoint, ContentType contentType, Object object) {
        return RestAssured.given().contentType(contentType).body(object).put(endpoint);
    }

}

package keywordDriven;

import api.APIManager;
import framework.Environment;
import io.restassured.response.Response;

public class ActionKeyword {
    private static final APIManager apiManager = APIManager.getInstance();
    private static final Environment environment = Environment.getInstance();

    public void setCredentials() {
        apiManager.setCredentials(environment.getUsernameKeywordDriven(), environment.getPasswordKeywordDriven());
    }

    public Response getRequest() {
        return apiManager.get(environment.getUserEndpoint());
    }
}

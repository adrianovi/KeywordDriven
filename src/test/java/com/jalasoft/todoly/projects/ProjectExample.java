package com.jalasoft.todoly.projects;

import api.APIManager;
import entities.NewProject;
import entities.Project;
import framework.Environment;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectExample {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();
    private final ArrayList<Integer> projectIds = new ArrayList<>();

    @BeforeClass
    public void setUp() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());

        NewProject newProject = new NewProject("New Project for Update", 1);
        Response responseUpdate = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, newProject);
        Project responseNewProject = responseUpdate.as(Project.class);
        projectIds.add(responseNewProject.getId());

        NewProject newProjectForDelete = new NewProject("New Project for Delete", 2);
        Response responseDelete = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, newProjectForDelete);
        Project responseNewProjectForDelete = responseDelete.as(Project.class);
        projectIds.add(responseNewProjectForDelete.getId());
    }

    @Test
    public void getAllProjects() {
        Reporter.log("Verify that a 200 OK status code and a correct response body result when a GET request to the \"/projects.json\" endpoint is executed", true);
        Response response = apiManager.get(environment.getProjectsEndpoint());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorMessage"), "Correct response body is returned");
        Assert.assertFalse(response.getBody().asString().contains("ErrorCode"), "Correct response body is not returned");
    }

    @Test
    public void createProject() {
        NewProject newProject = new NewProject("New Project Test", 2);
        Response response = apiManager.post(environment.getProjectsEndpoint(), ContentType.JSON, newProject);
        Project responseProject = response.as(Project.class);
        projectIds.add(responseProject.getId());

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseProject.getContent(), newProject.getContent(), "Incorrect Content value was set");
        Assert.assertEquals(responseProject.getIcon(), newProject.getIcon(), "Incorrect Icon value was set");
    }

    @Test
    public void updateProjectById() {
        Integer projectId = projectIds.get(0);
        String projectIdEndpoint = String.format(environment.getProjectByIdEndpoint(), projectId);
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("Icon", 7);

        Response response = apiManager.put(projectIdEndpoint, ContentType.JSON, jsonAsMap);
        Project responseProject = response.as(Project.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertEquals(responseProject.getIcon(), jsonAsMap.get("Icon"), "Incorrect Icon value was set");
    }

    @Test
    public void deleteProjectById() {
        Integer projectId = projectIds.get(1);
        String projectByIdEndpoint = String.format(environment.getProjectByIdEndpoint(), projectId);
        Response response = apiManager.delete(projectByIdEndpoint);
        Project responseProject = response.as(Project.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error Message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error Code was returned");
        Assert.assertTrue(responseProject.getDeleted(), "Project was not deleted");
    }

    @AfterClass
    public void tearDown() {
        for (Integer projectId : projectIds) {
            String projectIdEndpoint = String.format(environment.getProjectByIdEndpoint(), projectId);
            apiManager.delete(projectIdEndpoint);
        }
    }
}

package com.jalasoft.todoly.user;

import entities.User;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ReadExcelSheet;

public class UserKeywordDriven {
    private static final Environment environment = Environment.getInstance();
    private static ReadExcelSheet readExcelSheet = new ReadExcelSheet();

    @Test
    public void getUserUsingKeywordDriven() {
        readExcelSheet.readExcelData(4);
        Response response = readExcelSheet.getResponse();
        User responseUser = response.as(User.class);

        Assert.assertEquals(response.statusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.statusLine().contains("200 OK"), "Correct status code and message are not returned");
        Assert.assertFalse(response.body().asString().contains("ErrorCode"), "Correct response body is not returned");
        Assert.assertFalse(response.body().asString().contains("ErrorMessage"), "Correct response body is not returned");
        Assert.assertEquals(responseUser.getEmail(), environment.getUsernameKeywordDriven(), "Incorrect Email was returned");
    }
}

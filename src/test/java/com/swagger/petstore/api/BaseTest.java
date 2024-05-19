package com.swagger.petstore.api;

import com.swagger.petstore.api.utils.Constants;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

/**
 * Abstract base class for API tests.
 * Sets the base URI for the RestAssured requests.
 */
public abstract class BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Constants.BASE_URL;
    }
}

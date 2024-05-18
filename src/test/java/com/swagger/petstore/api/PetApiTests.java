package com.swagger.petstore.api;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetApiTests extends BaseTest {

    private static int petId;

    @BeforeClass
    public void setup() {
        super.setup();
        // Generate a random 3-digit petId
        petId = (int) (Math.random() * 900) + 100; // Generates a random number between 100 and 999
    }

    @DataProvider(name = "petData")
    public Object[][] petData() {
        return new Object[][]{
                {"Krupal", "sold", "test_tag"}
        };
    }

    @Test(description = "Get list of available pets")
    public void testGetAvailablePets() {
        given().
                queryParam("status", "available").
        when().
                get("/pet/findByStatus").
        then().
                assertThat().
                statusCode(200).
                body("status", everyItem(equalTo("available"))).
                body(matchesJsonSchemaInClasspath("schema/pet-schema.json")); // Validate against the JSON schema
    }

    @Test(description = "Update details of a pet", dependsOnMethods = {"testGetAvailablePets"}, dataProvider = "petData")
    public void testUpdatePetDetails(String name, String status, String tagName) {
        String requestBody = "{\"id\": " + petId + ", \"name\": \"" + name + "\", \"status\": \"" + status + "\", \"tags\": [{\"id\": 1, \"name\": \"" + tagName + "\"}]}";

        given().
                contentType("application/json").
                body(requestBody).
        when().
                put("/pet").
        then().
                assertThat().
                statusCode(200);
    }

    @Test(description = "Get details of a pet after put", dependsOnMethods = {"testUpdatePetDetails"}, dataProvider = "petData")
    public void testGetPetDetails(String name, String status, String tagName) {
        given().
                pathParam("petId", petId).
        when().
                get("/pet/{petId}").
        then().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("schema/put-pet-schema.json")). // Validate against the JSON schema
                body("id", equalTo(petId)).
                body("name", equalTo(name)).
                body("status", equalTo(status)).
                body("tags.name", hasItem(tagName));
    }
}
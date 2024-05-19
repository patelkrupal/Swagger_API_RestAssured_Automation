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

    /**
     * Test to get a list of available pets.
     * Sends a GET request to the /pet/findByStatus endpoint with a query parameter for status "available".
     * Asserts that the response status code is 200 and that all returned pets have a status of "available".
     * Validates the response against the JSON schema located at schema/pet-schema.json.
     */
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

    /**
     * Test to update the details of a pet.
     * Sends a PUT request to the /pet endpoint with a JSON body containing the pet's details.
     * Uses the petId generated in the setup method and data from the petData data provider.
     * Asserts that the response status code is 200.
     */
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

    /**
     * Test to get the details of a pet after updating it.
     * Sends a GET request to the /pet/{petId} endpoint using the petId.
     * Asserts that the response status code is 200 and that the returned pet's details match the expected values.
     * Validates the response against the JSON schema located at schema/put-pet-schema.json.
     */
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

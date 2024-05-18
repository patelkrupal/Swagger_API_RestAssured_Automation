# Pet API Tests

This project contains automated tests for the Pet API using Rest Assured framework,TestNG and Allure for generating Report.

## Prerequisites

- Java
- Maven

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/patelkrupal/Swagger_API_RestAssured_Automation.git
    ```

2. Navigate to the project directory:

    ```bash
    cd Swagger_API_RestAssured_Automation
    ```

3. Install dependencies:

    ```bash
    mvn clean install
    ```

## Running Tests

To run the tests, use the following Maven command:

```bash
mvn test
```

## Generating Allure Reports

To generate and view Allure reports for the test execution, use the following Maven command:

```bash
mvn allure:serve
```


## Additional Notes

## BaseTest.java Overview

BaseTest.java serves as the foundation for the project, abstracting out essential details such as:

- **Environment URL:** The `setup()` method in BaseTest.java configures the base URI for the project, representing the URL of the target system under test. Centralizing this configuration in the base class ensures consistency across all test cases.

- **Connection Details:** BaseTest.java manages the connection details necessary for interacting with the API. By initializing RestAssured in the `setup()` method, it establishes a connection to the target system, facilitating subsequent HTTP requests and responses in the test cases.


## PetApiTests.java Overview

PetApiTests.java contains the automated test cases for the Pet API using Rest Assured framework and TestNG. Below is an overview of its key features:

- **Test Setup:** The `setup()` method in PetApiTests.java initializes the test environment by invoking the setup method from the BaseTest class. This includes configuring the base URI for the API requests and generating a random 3-digit petId for use in the test cases.

- **Data Provider:** PetApiTests.java utilizes a data provider named `petData()` to supply test data for the `testUpdatePetDetails()` and `testGetPetDetails()` test methods. This data provider ensures that these test methods are executed with various combinations of input data.

- **Test Cases:** PetApiTests.java includes the following test cases:
   - **testGetAvailablePets():** This test case retrieves the list of available pets by sending a GET request to the /pet/findByStatus endpoint with the status parameter set to "available". It then validates the response status code and checks if every pet in the response has the status "available". Additionally, it validates the response body against the JSON schema defined in the `pet-schema.json` file.

   - **testUpdatePetDetails():** This test case updates the details of a pet by sending a PUT request to the /pet endpoint with the pet details in the request body. It uses the data provided by the `petData()` data provider to specify the pet's name, status, and tag name. The test then verifies that the response status code is 200, indicating a successful update.

   - **testGetPetDetails():** This test case retrieves the details of a pet after it has been updated. It sends a GET request to the /pet/{petId} endpoint, where the {petId} parameter is replaced with the randomly generated petId. The test verifies that the response status code is 200 and validates the response body against the JSON schema defined in the `put-pet-schema.json` file. It also checks if the returned pet details match the expected name, status, and tag name.


## Schema Directory

The Schema directory contains JSON schemas utilized for validating response bodies in the automated tests. Below is an overview of its purpose and contents:

- **Purpose:** JSON schemas serve as a blueprint for defining the structure and properties of JSON data. They are used to ensure that the responses received from the API adhere to a specific format and contain the expected fields and values.

- **Contents:**
   - **pet-schema.json:** This JSON schema defines the structure of the response body expected when retrieving a list of pets. It specifies the properties such as id, name, photoUrls, tags, and status, along with their respective data types and whether they are required or optional.

   - **put-pet-schema.json:** This JSON schema defines the structure of the response body expected after updating the details of a pet. It specifies similar properties as the pet-schema.json but may differ based on the specific requirements of the PUT endpoint.




## Note:

If the automation encounters a failure during execution, please make sure to comment out the following code from the `PetApiTests.java` file. Changes in JSON format might occur due to API ownership by Swagger.

```java
// Validate against the JSON schema
body(matchesJsonSchemaInClasspath("schema/pet-schema.json"));

// Validate against the JSON schema
body(matchesJsonSchemaInClasspath("schema/put-pet-schema.json"));
```


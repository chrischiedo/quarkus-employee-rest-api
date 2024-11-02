package dev.chiedo.employee;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;


@QuarkusTest
public class EmployeeResourceTest {

    private Employee createNewEmployee() {
        Employee employee = new Employee();

        employee.setFirstName(RandomStringUtils.randomAlphabetic(10));
        employee.setMiddleName(RandomStringUtils.randomAlphabetic(10));
        employee.setLastName(RandomStringUtils.randomAlphabetic(10));
        employee.setDepartment(RandomStringUtils.randomAlphabetic(10));
        employee.setEmailAddress(RandomStringUtils.randomAlphabetic(10) + "@example.com");
        employee.setPhoneNumber(RandomStringUtils.randomNumeric(10));

        return employee;
    }

    @Test
    public void testGetAllEmployeesEndpointReturnsStatusCode200() {
        given()
                .when().get("/api/v1/employees")
                .then()
                .statusCode(200);
    }

    @Test
    public void testCreateEmployeeFailsWithMissingFirstName() {
        Employee employee = createNewEmployee();

        employee.setFirstName(null);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(employee)
                .post("/api/v1/employees")
                .then()
                .statusCode(400);
    }
}

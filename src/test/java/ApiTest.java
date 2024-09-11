import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ApiTest {

    @Test
    @TmsLink("C1234")  // Link to the Test Management System test case ID
    @Description("Test to verify the status and response of a GET request to retrieve user details.")
    public void testGetUserDetails() {
        logStep("Sending GET request to retrieve user details.");
        given()
                .baseUri("https://api.privatbank.ua/p24api")
                .basePath("/pubinfo")
                .given().param("exchange&coursid", 5)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("ccy", notNullValue());
    }

    @Step("{0}")
    public void logStep(String step) {
        // Log steps to Allure report for better visibility
        System.out.println(step);
    }
}

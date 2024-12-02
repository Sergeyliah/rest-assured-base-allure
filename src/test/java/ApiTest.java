import assertions.AssertableResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.restassured.http.ContentType;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import services.UserApiService;
import utils.ConditionUtils;
import utils.Email;
import utils.RestAssuredUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class ApiTest extends BaseTest{
    UserApiService userApiService = new UserApiService();

    @Test
    @TmsLink("C1234")  // Link to the Test Management System test case ID
    @Description("Test to verify the status and response of a GET request to retrieve user details.")
    public void testGetUserDetails() {
        log.info("Sending GET request to retrieve user details.");
        logStep("Sending GET request to retrieve user details.");
        userApiService.getCurrencyRate()
                .then()
                .statusCode(200)
                .body("ccy", notNullValue())
                .body(matchesJsonSchemaInClasspath("userSchema.json"));
    }

    @Step("{0}")
    public void logStep(String step) {
        // Log steps to Allure report for better visibility
        System.out.println(step);
    }

    @Test
    @TmsLink("C12345")  // Link to the Test Management System test case ID
    @Description("Test to verify the status and response of a GET request to retrieve user details.")
    public void testGetUserDetails_UsingAssertableResponse() {
        int exchange = 5;
        AssertableResponse assertableResponse = userApiService.getCurrencyRate(exchange);
        assertableResponse
                .shouldHave(ConditionUtils.statusCode(200))
                .shouldHave(ConditionUtils.contentType(ContentType.JSON))
                .shouldHave(ConditionUtils.bodyField("ccy", notNullValue()));
        assertableResponse.getResponse().then()
                .body(matchesJsonSchemaInClasspath("userSchema.json"));
    }

    @Test
    @TmsLink("C12345")  // Link to the Test Management System test case ID
    @Description("get mail.")
    public void testGetMail() throws MessagingException {
        Email email = new Email();
        Message[] messages = email.getMassage();
        System.out.println(messages.length);
    }
}

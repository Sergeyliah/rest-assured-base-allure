package utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class RestAssuredUtils {
    static ProjectConfig config = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    public static void setBaseURI(){
        RestAssured.baseURI = config.baseUrl();;
    }

    public static void resetBaseURI(){
        RestAssured.baseURI = null;
    }

    public static void setAuthCookie(String cookie){
        RestAssured.requestSpecification = null;
        RestAssured.requestSpecification = new RequestSpecBuilder().addHeader("Cookie", cookie).build();
    }

    public static void resetAuthCookie(){
        RestAssured.requestSpecification = null;
        log.trace("Removing auth cookie. ");
    }

    public static RequestSpecification perform(){
        return RestAssured
                .given()
                .filters(getFilters());
    }

    private static List<Filter> getFilters() {
        if (config.logging()) {
            return Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
        }
        return Collections.singletonList(new AllureRestAssured());
    }
}

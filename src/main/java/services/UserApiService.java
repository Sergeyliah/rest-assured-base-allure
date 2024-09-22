package services;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.RestAssuredUtils;

public class UserApiService {

    @Step
    public Response getCurrencyRate(){
        return RestAssuredUtils.perform()
                .basePath("/pubinfo")
                .given().param("exchange&coursid", 5)
                .when()
                .get();
    }
}

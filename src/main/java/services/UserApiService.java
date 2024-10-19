package services;

import assertions.AssertableResponse;
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

    @Step
    public AssertableResponse getCurrencyRate(int exchange){
        return new AssertableResponse(RestAssuredUtils.perform()
                .basePath("/pubinfo")
                .given().param("exchange&coursid", exchange)
                .when()
                .get());
    }
}

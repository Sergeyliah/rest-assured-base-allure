package assertions;

import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AssertableResponse {
    private final Response response;

    @Step("then api response should have {condition}")
    public AssertableResponse shouldHave(Condition condition){
        condition.check(response);
        return this;
    }

    public <T> T mapToPojo(Class<T> tClass){
        return response.as(tClass);
    }

    public ResponseBodyExtractionOptions getBody(){
        return response.getBody();
    }

    public Headers getHeaders(){
        return response.getHeaders();
    }
}

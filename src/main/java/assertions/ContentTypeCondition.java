package assertions;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentTypeCondition implements Condition{
    private final ContentType contentType;

    @Override
    public void check(Response response){
        response.then().assertThat().contentType(contentType);
    }

    @Override
    public String toString(){
        return "content type is " + contentType;
    }
}

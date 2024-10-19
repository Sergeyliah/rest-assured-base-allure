package assertions;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matcher;

@RequiredArgsConstructor
public class BodyContentCondition implements Condition{
    private final Matcher<?> matcher;

    @Override
    public void check(Response response){
        response.then().assertThat().body(matcher);
    }
}

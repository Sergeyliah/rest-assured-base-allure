package assertions;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matcher;

@RequiredArgsConstructor
public class HeaderCondition implements Condition{
    private final String headerName;
    private final Matcher matcher;

    @Override
    public void check(Response response){
        response.then().assertThat().header(headerName, matcher);
    }
}

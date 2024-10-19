package utils;

import assertions.*;
import io.restassured.http.ContentType;
import lombok.experimental.UtilityClass;
import org.hamcrest.Matcher;

@UtilityClass
public class ConditionUtils {
    public StatusCodeCondition statusCode(int code){
        return new StatusCodeCondition(code);
    }

    public FieldCondition bodyField(String jsonPath, Matcher matcher){
        return new FieldCondition(jsonPath, matcher);
    }

    public ContentTypeCondition contentType(ContentType contentType){
        return new ContentTypeCondition(contentType);
    }

    public HeaderCondition headerValue(String name, Matcher<?> matcher){
        return new HeaderCondition(name, matcher);
    }

    public BodyContentCondition bodyContent(Matcher<?> matcher){
        return new BodyContentCondition(matcher);
    }
}

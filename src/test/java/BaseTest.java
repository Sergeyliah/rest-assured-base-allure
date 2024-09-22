import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utils.RestAssuredUtils;

@Slf4j
public class BaseTest {
    @BeforeAll
    public static void initializeTestSetup(){
        RestAssuredUtils.setBaseURI();
    }

    @AfterAll
    public static void tearTestEnvironment(){
        RestAssuredUtils.resetBaseURI();
        //RestAssuredUtils.resetAuthCookie();
    }

}

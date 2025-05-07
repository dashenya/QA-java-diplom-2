import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;


public class UserOrdersWithoutAuthTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

    }

    @Test
    @DisplayName("Get user orders without auth token")
    @Description("Basic negative test for /api/orders endpoint")
    public void getUserOrdersWithoutAccessTokenCheckBodyAndStatusCode() {

        Response response = Order.sendGetRequestUserOrdersWithoutAccessToken();
        //response.getBody().prettyPrint();
        Methods.compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
        Methods.compareFieldToBoolean(response, "success", false);
        Methods.compareFieldToString(response, "message", "You should be authorised");

    }

}

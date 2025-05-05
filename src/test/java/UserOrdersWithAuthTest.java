import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class UserOrdersWithAuthTest {
    private List<String> ingredients;
    private Order order;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        order = new Order();
        ingredients = new ArrayList<>();
        accessToken = User.registerUserAndGetAccessToken();
    }

    @Test
    @DisplayName("Get user orders with auth token")
    @Description("Basic positive test for /api/orders endpoint")
    public void getUserOrdersWithAccessTokenCheckBodyAndStatusCode() {

        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa6e");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        order.setIngredients(ingredients);

        Response response = Order.sendPostRequestCreateOrderWithAccessToken(accessToken, order);
        response.getBody().prettyPrint();

        Response response2 = Order.sendGetRequestUserOrdersWithAccessToken(accessToken);
        response2.getBody().prettyPrint();
        Methods.compareFieldSize(response2,"orders", 1);
        Methods.compareStatusCode(response2, HttpStatus.SC_OK);

    }

    @Test
    @DisplayName("Get user orders (who has no orders) with auth token")
    @Description("Basic positive test for /api/orders endpoint")
    public void getEmptyUserOrdersWithAccessTokenCheckBodyAndStatusCode() {

        Response response = Order.sendGetRequestUserOrdersWithAccessToken(accessToken);
        response.getBody().prettyPrint();
        Methods.compareFieldIsEmpty(response,"orders");
        Methods.compareStatusCode(response, HttpStatus.SC_OK);

    }

    @After
    public void delete() {
        User.deleteUser(accessToken);
    }
}

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class CreateOrderTest {
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
    @DisplayName("Check creation of order without access token")
    @Description("Basic negative test for /api/orders endpoint")
    public void createOrderWithoutAccessTokenCheckBodyAndStatusCode() {

        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        order.setIngredients(ingredients);

        Response response = Order.sendPostRequestCreateOrderWithoutAccessToken(order);
        response.getBody().prettyPrint();
        Methods.compareStatusCode(response, HttpStatus.SC_FORBIDDEN);

    }

    @After
    public void delete() {
        User.deleteUser(accessToken);
    }
}

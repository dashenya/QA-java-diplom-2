import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    private final List<String> ingredients;
    private Order order;
    private String accessToken;

    public CreateOrderParametrizedTest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Parameterized.Parameters
    public static Object[][] getParameters() {
        return new Object[][]{
                {List.of("61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6c")},
                {List.of("BLA", "BLA")},
                {List.of()},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        order = new Order();
        accessToken = User.registerUserAndGetAccessToken();
    }

    @Test
    @DisplayName("Check creation of order (send different ingredients data)")
    @Description("Basic test for /api/orders endpoint")
    public void createOrderWithDifferentIngredientsCheckBodyAndStatusCode() {

        order.setIngredients(ingredients);

        Response response2 = Order.sendPostRequestCreateOrderWithAccessToken(accessToken, order);
        response2.getBody().prettyPrint();

        if (ingredients.isEmpty()) {
            Methods.compareStatusCode(response2, HttpStatus.SC_BAD_REQUEST);
            Methods.compareFieldToBoolean(response2, "success", false);
            Methods.compareFieldToString(response2, "message", "Ingredient ids must be provided");
        }
        if (ingredients.contains("BLA")) {
            Methods.compareStatusCode(response2, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        if (!ingredients.isEmpty() && !ingredients.contains("BLA")) {
            Methods.compareStatusCode(response2, HttpStatus.SC_OK);
            Methods.compareFieldNotNull(response2, "order");
        }

    }

    @After
    public void delete() {
        User.deleteUser(accessToken);
    }

}

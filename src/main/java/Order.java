import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Step("Send POST request to /api/v1/orders with access token")
    public static Response sendPostRequestCreateOrderWithAccessToken(String accessToken, Order order){
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post("/api/orders");
        return response;
    }

    @Step("Send GET request to /api/v1/orders with access token")
    public static Response sendGetRequestUserOrdersWithAccessToken(String accessToken){
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders");
        return response;
    }

    @Step("Send GET request to /api/v1/orders with access token")
    public static Response sendGetRequestUserOrdersWithoutAccessToken(){
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/orders");
        return response;
    }

    @Step("Send POST request to /api/v1/orders without access token")
    public static Response sendPostRequestCreateOrderWithoutAccessToken(Order order){
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .body(order)
                .when()
                .post("/api/orders");
        return response;
    }
}

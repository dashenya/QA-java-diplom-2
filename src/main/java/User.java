import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class User {

    private String email;
    private String password;
    private String name;

    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User generatingUser() {

        String userLogin = Methods.generatingRandomStringBounded();
        User user = new User(userLogin + "@yandex.ru", "password", "userName");
        return user;

    }

    @Step("Send POST request to /api/auth/register")
    public static Response sendPostRequestCreateUser(User user) {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
        return response;
    }

    @Step("Send POST request to /api/auth/login")
    public static Response sendPostRequestLoginUser(User user) {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post("api/auth/login");
        return response;
    }

    @Step("Get accessToken")
    public static String getAccessToken(Response response) {

        JsonPath jsonPathEvaluator = response.jsonPath();

        String accessToken = jsonPathEvaluator.get("accessToken");
        System.out.println(accessToken);
        return accessToken;

    }

    @Step("Register user and get access token")
    public static String registerUserAndGetAccessToken() {
        User user = generatingUser();
        Response response = sendPostRequestCreateUser(user);
        String accessToken = getAccessToken(response);
        return accessToken;
    }

    @Step("Send PATCH request to /api/auth/user for change data with accessToken")
    public static Response sendPatchRequestChangeDataUser(String accessToken, User userData) {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .and()
                .body(userData)
                .when()
                .patch("api/auth/user");
        return response;
    }

    @Step("Send PATCH request to /api/auth/user for change data without accessToken")
    public static Response sendPatchRequestChangeDataUserWithoutAccessToken(User userData) {
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .body(userData)
                .when()
                .patch("api/auth/user");
        return response;
    }

    @Step("Delete test user")
    public static void deleteUser(String accessToken) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

}

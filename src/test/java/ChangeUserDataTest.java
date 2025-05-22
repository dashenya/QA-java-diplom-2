import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ChangeUserDataTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        accessToken = User.registerUserAndGetAccessToken();
    }

    @Test
    @DisplayName("Check changing user name (status code 200)")
    @Description("Basic positive test for PATCH /api/auth/user endpoint")
    public void changeUserNameWithAccessTokenCheckBodyAndStatusCode200() {

        User updatedUserData = new User();
        updatedUserData.setName("Aloha");

        Response response2 = User.sendPatchRequestChangeDataUser(accessToken, updatedUserData);
        response2.prettyPrint();

        // Проверка в ответе user.name
        AuthUserResponse responseAfterChange = response2.as(AuthUserResponse.class);
        Assert.assertEquals("Aloha", responseAfterChange.getUser().getName());

        Methods.compareFieldToBoolean(response2, "success", true);
        Methods.compareStatusCode(response2, HttpStatus.SC_OK);

    }

    @Test
    @DisplayName("Check changing user email (status code 200)")
    @Description("Basic positive test for PATCH /api/auth/user endpoint")
    public void changeUserEmailWithAccessTokenCheckBodyAndStatusCode200() {

            User updatedUserData = new User();
            String newUserLogin = Methods.generatingRandomStringBounded() + "@yandex.ru";
            updatedUserData.setEmail(newUserLogin);

            Response response2 = User.sendPatchRequestChangeDataUser(accessToken, updatedUserData);
            response2.getBody().prettyPrint();

            // Проверка в ответе user.email
            AuthUserResponse responseAfterChange = response2.as(AuthUserResponse.class);
            Assert.assertEquals(newUserLogin.toLowerCase(), responseAfterChange.getUser().getEmail());

            Methods.compareFieldToBoolean(response2, "success", true);
            Methods.compareStatusCode(response2, HttpStatus.SC_OK);

    }

    @Test
    @DisplayName("Check changing user name without accessToken (status code 401)")
    @Description("Basic negative test for PATCH /api/auth/user endpoint")
    public void changeUserNameWithoutAccessTokenCheckBodyAndStatusCode401() {

            User updatedUserData = new User();
            updatedUserData.setName("Aloha");

            Response response2 = User.sendPatchRequestChangeDataUserWithoutAccessToken(updatedUserData);
            response2.prettyPrint();

            Methods.compareFieldToBoolean(response2, "success", false);
            Methods.compareFieldToString(response2, "message", "You should be authorised");
            Methods.compareStatusCode(response2, HttpStatus.SC_UNAUTHORIZED);

    }

    @After
    public void delete() {
        User.deleteUser(accessToken);
    }
}

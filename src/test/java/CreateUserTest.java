import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateUserTest {
    private String userLogin;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        // Генерация логина
        userLogin = Methods.generatingRandomStringBounded();
    }

    @Test
    @DisplayName("Check creation of user (send all obligatory fields check status code 200)")
    @Description("Basic positive test for /api/auth/register endpoint")
    public void createUserWithAllObligatoryFieldsAndCheckBodyAndStatusCode200() {

        // Будущее body запроса
        User user = new User(userLogin + "@yandex.ru", "password", "userName");

        // Отправка запроса на создание пользователя
        Response response = User.sendPostRequestCreateUser(user);
        // Проверка в ответе user.name и user.email
        AuthUserResponse authUserResponse = response.as(AuthUserResponse.class);
        Assert.assertEquals("userName", authUserResponse.getUser().getName());
        Assert.assertEquals(userLogin.toLowerCase() + "@yandex.ru", authUserResponse.getUser().getEmail());

        // Проверка в ответе success = true
        Methods.compareFieldToBoolean(response, "success", true);
        // Проверка статус код = 200
        Methods.compareStatusCode(response, HttpStatus.SC_OK);

        // Удаление пользователя
        String token = User.getAccessToken(response);
        User.deleteUser(token);
    }

    @Test
    @DisplayName("Check creation of user that has already been created, status code 403")
    @Description("Basic negative test for /api/auth/register endpoint")
    public void createUserThatHasAlreadyBeenCreatedCheckBodyAndStatusCode403() {

        // Будущее body запроса
        User user = new User(userLogin + "@yandex.ru", "password", "userName");

        // Отправка запроса на создание пользователя
        Response response1 = User.sendPostRequestCreateUser(user);
        // Достаем accessToken, который понадобится для удаления пользователя
        String token = User.getAccessToken(response1);

        // Повторная отправка тех же параметров (должна привести к ошибке, что пользовтель уже создан)
        Response response2 = User.sendPostRequestCreateUser(user);
        // Проверка в ответе success = false
        Methods.compareFieldToBoolean(response2, "success", false);
        // Проверка статус код = 403
        Methods.compareStatusCode(response2, HttpStatus.SC_FORBIDDEN);

        // Удаление пользователя
        User.deleteUser(token);
    }

    @Test
    @DisplayName("Check creation of user without obligatory field email, status code 403")
    @Description("Basic negative test for /api/auth/register endpoint")
    public void createUserWithoutEmailFieldCheckBodyAndStatusCode403() {

        // Будущее body запроса
        User user = new User();
        user.setName("userName");
        user.setPassword("password");

        // Отправка запроса на создание пользователя
        Response response = User.sendPostRequestCreateUser(user);

        // Проверка в ответе success = false
        Methods.compareFieldToBoolean(response, "success", false);
        // Проверка в ответе message
        Methods.compareFieldToString(response, "message", "Email, password and name are required fields");
        // Проверка статус код = 403
        Methods.compareStatusCode(response, HttpStatus.SC_FORBIDDEN);

    }

    @Test
    @DisplayName("Check creation of user without obligatory field password, status code 403")
    @Description("Basic negative test for /api/auth/register endpoint")
    public void createUserWithoutPasswordFieldCheckBodyAndStatusCode403() {

        // Будущее body запроса
        User user = new User();
        user.setEmail(userLogin + "@yandex.ru");
        user.setName("userName");

        // Отправка запроса на создание пользователя
        Response response = User.sendPostRequestCreateUser(user);

        // Проверка в ответе success = false
        Methods.compareFieldToBoolean(response, "success", false);
        // Проверка в ответе message
        Methods.compareFieldToString(response, "message", "Email, password and name are required fields");
        // Проверка статус код = 403
        Methods.compareStatusCode(response, HttpStatus.SC_FORBIDDEN);

    }

    @Test
    @DisplayName("Check creation of user without obligatory field name, status code 403")
    @Description("Basic negative test for /api/auth/register endpoint")
    public void createUserWithoutNameFieldCheckBodyAndStatusCode403() {

        // Будущее body запроса
        User user = new User();
        user.setEmail(userLogin + "@yandex.ru");
        user.setPassword("password");

        // Отправка запроса на создание пользователя
        Response response = User.sendPostRequestCreateUser(user);

        // Проверка в ответе success = false
        Methods.compareFieldToBoolean(response, "success", false);
        // Проверка в ответе message
        Methods.compareFieldToString(response, "message", "Email, password and name are required fields");
        // Проверка статус код = 403
        Methods.compareStatusCode(response, HttpStatus.SC_FORBIDDEN);

    }
}

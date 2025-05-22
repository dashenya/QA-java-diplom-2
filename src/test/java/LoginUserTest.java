import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;


public class LoginUserTest {
    private String userLogin;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        // Генерация логина
        userLogin = Methods.generatingRandomStringBounded();
    }

    @Test
    @DisplayName("Check login of user (send all obligatory fields check status code 200)")
    @Description("Basic positive test for /api/auth/login endpoint")
    public void loginExistedUserCheckBodyAndStatusCode200() {

        // Будущее body запроса
        User user = new User(userLogin + "@yandex.ru", "password", "userName");

        // Отправка запроса на создание пользователя
        User.sendPostRequestCreateUser(user);

        Response response = User.sendPostRequestLoginUser(user);
        // Проверка в ответе success = true
        Methods.compareFieldToBoolean(response, "success", true);
        // Проверка статус код = 200
        Methods.compareStatusCode(response, HttpStatus.SC_OK);

        // Удаление пользователя
        String token = User.getAccessToken(response);
        User.deleteUser(token);
    }

    @Test
    @DisplayName("Check login of unexisted user, status code 403")
    @Description("Basic negative test for /api/auth/login endpoint")
    public void loginUnexistedUserCheckBodyAndStatusCode401() {

        // Будущее body запроса
        User user = new User();
        user.setEmail(userLogin + "@yandex.ru");
        user.setPassword("password");

        // Отправка запроса на логин пользователя
        Response response = User.sendPostRequestLoginUser(user);

        // Проверка в ответе success = false
        Methods.compareFieldToBoolean(response, "success", false);
        // Проверка в ответе message
        Methods.compareFieldToString(response, "message", "email or password are incorrect");
        // Проверка статус код = 401
        Methods.compareStatusCode(response, HttpStatus.SC_UNAUTHORIZED);

    }
}

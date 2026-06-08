import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierCredentials;
import model.CourierModel;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import io.restassured.response.Response;
import static steps.CourierSteps.deleteCourier;
import static data.CourierData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourier;

public class LoginCourierTest extends BaseApiTest {

        private CourierModel courier;
        private CourierCredentials credentials;

        @Before
        public void createCourierBeforeTest() {
            String login = LOGIN + System.currentTimeMillis();

            courier = new CourierModel(login, PASSWORD, FIRSTNAME);
            createCourier(courier);

            credentials = new CourierCredentials(login, PASSWORD);
        }

    @After
    public void cleanUp() {
        if (credentials != null) {
            Response response = loginCourier(credentials);

            if (response.statusCode() == HTTP_OK) {
                int courierId = response.path("id");
                deleteCourier(courierId);
            }
        }
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверка, что курьер может авторизоваться с корректными логином и паролем. В ответе возвращается id")
    public void loginCourierSuccess() {

        loginCourier(credentials)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
    }
    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    @Description("Если не передать логин, возвращается ошибка")
    public void loginCourierWithoutLoginReturnsError() {
        CourierCredentials credentialsWithoutLogin = new CourierCredentials(null, PASSWORD);

        loginCourier(credentialsWithoutLogin)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Ошибка при авторизации без пароля")
    @Description("При авторизации без пароля сервис возвращает 504 Gateway Timeout — известный баг API")
    public void loginCourierWithoutPasswordReturnsError() {

        CourierCredentials credentials =
                new CourierCredentials(LOGIN, null);

        loginCourier(credentials)
                .then()
                .log().all()
                .statusCode(HTTP_GATEWAY_TIMEOUT);
    }

    @Test
    @DisplayName("Нельзя авторизоваться под несуществующим пользователем")
    @Description("Если передать логин и пароль несуществующего курьера, возвращается ошибка")
    public void loginNonExistentCourierReturnsError() {
        CourierCredentials credentials = new CourierCredentials(
                LOGIN + System.currentTimeMillis(),
                PASSWORD
        );

        loginCourier(credentials)
                .then()
                .log().all()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неверной парой логин и пароль")
    @Description("Если передать существующий логин и неверный пароль, возвращается ошибка")
    public void loginCourierWithWrongCredentialsReturnsError() {

        CourierCredentials wrongCredentilas = new CourierCredentials( courier.getLogin(),
                PASSWORD + "wrong");

        loginCourier(wrongCredentilas)
                .then()
                .log().all()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

}

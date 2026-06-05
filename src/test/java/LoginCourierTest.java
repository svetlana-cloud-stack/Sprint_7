import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierCredentials;
import model.CourierModel;
import org.junit.Test;

import static data.CourierData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.loginCourier;

public class LoginCourierTest extends BaseApiTest {

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверка, что курьер может авторизоваться с корректными логином и паролем. В ответе возвращается id")
    public void loginCourierSuccess() {
        String login = LOGIN + System.currentTimeMillis();

        CourierModel courier = new CourierModel(login, PASSWORD, FIRSTNAME);

        courierForDelete = courier;

        createCourier(courier);

        CourierCredentials credentials = new CourierCredentials(login, PASSWORD);

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
        CourierCredentials credentials = new CourierCredentials(null, PASSWORD);

        loginCourier(credentials)
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
        String login = LOGIN + System.currentTimeMillis();

        CourierModel courier = new CourierModel(login, PASSWORD, FIRSTNAME);

        courierForDelete = courier;

        createCourier(courier);

        CourierCredentials credentials = new CourierCredentials(login, PASSWORD + "wrong");

        loginCourier(credentials)
                .then()
                .log().all()
                .statusCode(HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

}

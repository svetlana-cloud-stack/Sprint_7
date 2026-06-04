import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.CourierModel;
import org.junit.Test;

import static data.CourierData.*;
import static java.net.HttpURLConnection.*;
import static steps.CourierSteps.createCourier;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest extends BaseApiTest {

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка успешного создания курьера с валидными данными. Ожидается код ответа 201 и значение поля ok=true")
    public void createCourierSuccess() {
        CourierModel courier = new CourierModel(LOGIN + System.currentTimeMillis(), PASSWORD, FIRSTNAME);

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("При повторном создании курьера с тем же логином возвращается ошибка")
    public void createDuplicateCourierReturnsError() {
        CourierModel courier = new CourierModel(LOGIN + System.currentTimeMillis(), PASSWORD, FIRSTNAME);

        createCourier(courier);

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Если не передать логин, возвращается ошибка")
    public void createCourierWithoutLoginReturnsError() {
        CourierModel courier = new CourierModel(null, PASSWORD, FIRSTNAME);

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Если не передать пароль, возвращается ошибка")
    public void createCourierWithoutPasswordReturnsError() {
        CourierModel courier = new CourierModel(LOGIN, null, FIRSTNAME);

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Курьер успешно создаётся, если поле firstName не передано")
    public void createCourierWithoutFirstNameSuccess() {

        CourierModel courier = new CourierModel(
                LOGIN + System.currentTimeMillis(),
                PASSWORD,
                null
        );

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(HTTP_CREATED)
                .body("ok", equalTo(true));
    }

}

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.notNullValue;
import static steps.OrderSteps.getOrders;

public class GetOrdersTest extends BaseApiTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов")
    public void getOrdersReturnsOrdersList() {

        getOrders()
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("orders", notNullValue());
    }
}
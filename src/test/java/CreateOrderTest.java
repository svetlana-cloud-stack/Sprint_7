import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.OrderModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static steps.OrderSteps.createOrder;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseApiTest {

    private final List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Проверка создания заказа с цветом BLACK, GREY, двумя цветами и без указания цвета. В ответе возвращается track")
    public void createOrderWithDifferentColorsSuccess() {
        OrderModel order = new OrderModel(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2026-06-06",
                "Saske, come back to Konoha",
                color
        );

        createOrder(order)
                .then()
                .log().all()
                .statusCode(HTTP_CREATED)
                .body("track", notNullValue());
    }
}

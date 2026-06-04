package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;

import static data.CourierData.CREATE_ORDER_PATH;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    public static Response createOrder(OrderModel order) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .extract().response();
    }
}

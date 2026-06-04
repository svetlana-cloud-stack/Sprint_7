package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierModel;

import static data.CourierData.CREATE_COURIER_PATH;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    public static Response createCourier(CourierModel courier) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then()
                .extract().response();
    }
}

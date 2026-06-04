package steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.CourierModel;

import static data.CourierData.CREATE_COURIER_PATH;
import static io.restassured.RestAssured.given;
import model.CourierCredentials;
import static data.CourierData.LOGIN_COURIER_PATH;

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

    public static Response loginCourier(CourierCredentials credentials) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(LOGIN_COURIER_PATH)
                .then()
                .extract().response();
    }
}

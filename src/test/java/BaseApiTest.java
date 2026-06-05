import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static data.CourierData.BASE_URI;
import io.restassured.response.Response;
import model.CourierCredentials;
import model.CourierModel;
import org.junit.After;

import static java.net.HttpURLConnection.HTTP_OK;
import static steps.CourierSteps.deleteCourier;
import static steps.CourierSteps.loginCourier;

public class BaseApiTest {

    protected CourierModel courierForDelete;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @After
    public void cleanUp() {
        if (courierForDelete != null) {

            CourierCredentials credentials = new CourierCredentials(
                    courierForDelete.getLogin(),
                    courierForDelete.getPassword()
            );

            Response response = loginCourier(credentials);

            if (response.statusCode() == HTTP_OK) {
                int courierId = response.path("id");
                deleteCourier(courierId);
            }
        }
    }
}

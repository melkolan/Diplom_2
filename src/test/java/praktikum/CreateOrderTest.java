package praktikum;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseTest {

    @Test
    public void createOrderWithAuthorizationShouldReturnSuccess() {
        Map<String, String> user = UserGenerator.getRandomUser();

        Response registerResponse = registerUser(user);
        accessToken = registerResponse.then().extract().path("accessToken");

        List<String> ingredientIds = getIngredientIds(2);

        createOrderAuthorized(ingredientIds, accessToken).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthorizationShouldReturnSuccess() {
        List<String> ingredientIds = getIngredientIds(2);

        createOrder(ingredientIds).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    public void createOrderWithIngredientsShouldReturnSuccess() {
        List<String> ingredientIds = getIngredientIds(2);

        createOrder(ingredientIds).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutIngredientsShouldReturn400() {
        createOrder(Collections.emptyList()).then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidIngredientHashShouldReturn400() {
        createOrder(Collections.singletonList("invalid_hash")).then()
                .statusCode(400)
                .body("success", equalTo(false));
    }
}
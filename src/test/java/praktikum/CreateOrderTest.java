package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import praktikum.dto.request.OrderRequest;

import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends BaseTest {

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка успешного создания заказа неавторизованным пользователем с валидными ингредиентами")
    public void createOrderWithoutAuthorizationShouldReturnSuccess() {
        List<String> ingredientIds = getIngredientIds(2);
        OrderRequest request = buildOrderRequest(ingredientIds);

        ordersApi.createOrder(request).then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка успешного создания заказа при передаче списка валидных ингредиентов")
    public void createOrderWithIngredientsShouldReturnSuccess() {
        List<String> ingredientIds = getIngredientIds(2);
        OrderRequest request = buildOrderRequest(ingredientIds);

        ordersApi.createOrder(request).then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без передачи ингредиентов")
    public void createOrderWithoutIngredientsShouldReturn400() {
        OrderRequest request = buildOrderRequest(Collections.emptyList());

        ordersApi.createOrder(request).then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Description("Проверка ошибки сервера при создании заказа с невалидным хешем ингредиента") // Тест соответствует спецификации, но на стенде обнаружено несоответствие документации и фактического ответа API: вместо 500 сервис возвращает 400.
    public void createOrderWithInvalidIngredientHashShouldReturn500() {
        OrderRequest request = buildOrderRequest(Collections.singletonList("invalid_hash"));

        ordersApi.createOrder(request).then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
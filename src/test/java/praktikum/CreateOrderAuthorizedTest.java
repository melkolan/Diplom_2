package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import praktikum.dto.request.OrderRequest;
import praktikum.dto.request.RegisterUserRequest;
import praktikum.util.UserGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderAuthorizedTest extends BaseTest {

    @Before
    public void createUser() {
        RegisterUserRequest user = UserGenerator.getRandomUser();
        registerAndAuthorizeUser(user);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем с валидными ингредиентами")
    public void createOrderWithAuthorizationShouldReturnSuccess() {
        List<String> ingredientIds = getIngredientIds(2);
        OrderRequest request = buildOrderRequest(ingredientIds);

        ordersApi.createOrderAuthorized(request, accessToken).then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }
}
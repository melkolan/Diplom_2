package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import praktikum.dto.request.RegisterUserRequest;
import praktikum.util.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RegisterUserTest extends BaseTest {

    @Test
    @DisplayName("Регистрация уникального пользователя")
    @Description("Проверка успешной регистрации нового уникального пользователя")
    public void createUniqueUserShouldReturnSuccess() {
        RegisterUserRequest newUser = UserGenerator.getRandomUser();

        Response response = authApi.registerUser(newUser);
        accessToken = response.then().extract().path("accessToken");

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newUser.getEmail()))
                .body("user.name", equalTo(newUser.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
}
package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import praktikum.dto.request.LoginUserRequest;
import praktikum.dto.request.RegisterUserRequest;
import praktikum.util.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends BaseTest {

    private RegisterUserRequest user;

    @Before
    public void createUser() {
        user = UserGenerator.getRandomUser();
        registerAndAuthorizeUser(user);
    }

    @Test
    @DisplayName("Логин существующего пользователя")
    @Description("Проверка успешной авторизации ранее зарегистрированного пользователя")
    public void loginWithExistingUserShouldReturnSuccess() {
        LoginUserRequest request = buildLoginRequest(user.getEmail(), user.getPassword());

        authApi.loginUser(request).then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка ошибки авторизации при попытке входа с корректным email и неверным паролем")
    public void loginWithInvalidPasswordShouldReturn401() {
        LoginUserRequest request = buildLoginRequest(user.getEmail(), user.getPassword() + "_wrong");

        authApi.loginUser(request).then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверка ошибки авторизации при попытке входа с несуществующим email")
    public void loginWithInvalidEmailShouldReturn401() {
        LoginUserRequest request = buildLoginRequest("wrong_" + user.getEmail(), user.getPassword());

        authApi.loginUser(request).then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import praktikum.dto.request.RegisterUserRequest;
import praktikum.util.UserGenerator;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

public class RegisterDuplicateUserTest extends BaseTest {

    private RegisterUserRequest existingUser;

    @Before
    public void createUser() {
        existingUser = UserGenerator.getRandomUser();
        registerAndAuthorizeUser(existingUser);
    }

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    @Description("Проверка ошибки регистрации пользователя с уже существующими учетными данными")
    public void createDuplicateUserShouldReturn403() {
        authApi.registerUser(existingUser).then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}
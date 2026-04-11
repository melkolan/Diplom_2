package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.dto.request.RegisterUserRequest;
import praktikum.util.UserGenerator;

import java.util.Arrays;
import java.util.Collection;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class RegisterUserRequiredFieldTest extends BaseTest {

    private final String missingField;

    public RegisterUserRequiredFieldTest(String missingField) {
        this.missingField = missingField;
    }

    @Parameterized.Parameters(name = "Отсутствует поле: {0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {"email"},
                {"password"},
                {"name"}
        });
    }

    @Test
    @DisplayName("Регистрация без обязательного поля")
    @Description("Проверка ошибки регистрации при отсутствии одного из обязательных полей")
    public void createUserWithoutRequiredFieldShouldReturn403() {
        RegisterUserRequest user = UserGenerator.getRandomUser();

        switch (missingField) {
            case "email":
                user.setEmail(null);
                break;
            case "password":
                user.setPassword(null);
                break;
            case "name":
                user.setName(null);
                break;
            default:
                throw new IllegalArgumentException("Неизвестное обязательное поле: " + missingField);
        }

        authApi.registerUser(user).then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
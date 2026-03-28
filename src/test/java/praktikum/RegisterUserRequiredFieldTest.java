package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

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
    public void createUserWithoutRequiredFieldShouldReturn403() {
        Map<String, String> user = UserGenerator.getRandomUser();
        user.remove(missingField);

        registerUser(user).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
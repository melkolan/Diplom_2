package praktikum;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends BaseTest {

    @Test
    public void loginWithExistingUserShouldReturnSuccess() {
        Map<String, String> user = UserGenerator.getRandomUser();

        Response registerResponse = registerUser(user);
        accessToken = registerResponse.then().extract().path("accessToken");

        loginUser(user.get("email"), user.get("password")).then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.get("email")))
                .body("user.name", equalTo(user.get("name")))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void loginWithInvalidPasswordShouldReturn401() {
        Map<String, String> user = UserGenerator.getRandomUser();

        Response registerResponse = registerUser(user);
        accessToken = registerResponse.then().extract().path("accessToken");

        loginUser(user.get("email"), user.get("password") + "_wrong").then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
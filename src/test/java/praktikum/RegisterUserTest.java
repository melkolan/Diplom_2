package praktikum;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RegisterUserTest extends BaseTest {

    @Test
    public void createUniqueUserShouldReturnSuccess() {
        Map<String, String> user = UserGenerator.getRandomUser();

        Response response = registerUser(user);
        accessToken = response.then().extract().path("accessToken");

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.get("email")))
                .body("user.name", equalTo(user.get("name")))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    public void createDuplicateUserShouldReturn403() {
        Map<String, String> user = UserGenerator.getRandomUser();

        Response firstResponse = registerUser(user);
        accessToken = firstResponse.then().extract().path("accessToken");

        registerUser(user).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}
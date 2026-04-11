package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.constants.Endpoints;

import static io.restassured.RestAssured.given;

public class UserApi {

    private final RequestSpecification requestSpec;

    public UserApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .spec(requestSpec)
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.USER);
    }
}
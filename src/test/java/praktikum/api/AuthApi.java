package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.constants.Endpoints;
import praktikum.dto.request.LoginUserRequest;
import praktikum.dto.request.RegisterUserRequest;

import static io.restassured.RestAssured.given;

public class AuthApi {

    private final RequestSpecification requestSpec;

    public AuthApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Зарегистрировать пользователя")
    public Response registerUser(RegisterUserRequest request) {
        return given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post(Endpoints.REGISTER);
    }

    @Step("Авторизовать пользователя")
    public Response loginUser(LoginUserRequest request) {
        return given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post(Endpoints.LOGIN);
    }
}
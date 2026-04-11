package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.constants.Endpoints;

import static io.restassured.RestAssured.given;

public class IngredientsApi {

    private final RequestSpecification requestSpec;

    public IngredientsApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(requestSpec)
                .when()
                .get(Endpoints.INGREDIENTS);
    }
}
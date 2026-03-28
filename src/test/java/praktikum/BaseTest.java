package praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseTest {

    protected RequestSpecification requestSpec;
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        RestAssured.basePath = "/api";

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
    }

    @Step("Создать пользователя")
    protected Response registerUser(Map<String, String> userData) {
        return given()
                .spec(requestSpec)
                .body(userData)
                .when()
                .post("/auth/register");
    }

    @Step("Логин пользователя")
    protected Response loginUser(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/login");
    }

    @Step("Удалить пользователя")
    protected Response deleteUser(String token) {
        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .when()
                .delete("/auth/user");
    }

    @Step("Получить id ингредиентов")
    protected List<String> getIngredientIds(int count) {
        List<String> allIds = given()
                .spec(requestSpec)
                .when()
                .get("/ingredients")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data._id");

        if (allIds == null || allIds.size() < count) {
            throw new IllegalStateException("Недостаточно ингредиентов для теста");
        }

        return new ArrayList<>(allIds.subList(0, count));
    }

    @Step("Создать заказ без авторизации")
    protected Response createOrder(List<String> ingredientIds) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredientIds);

        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/orders");
    }

    @Step("Создать заказ с авторизацией")
    protected Response createOrderAuthorized(List<String> ingredientIds, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredientIds);

        return given()
                .spec(requestSpec)
                .header("Authorization", token)
                .body(body)
                .when()
                .post("/orders");
    }
}
package praktikum;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import praktikum.api.AuthApi;
import praktikum.api.IngredientsApi;
import praktikum.api.OrdersApi;
import praktikum.api.UserApi;
import praktikum.dto.request.LoginUserRequest;
import praktikum.dto.request.OrderRequest;
import praktikum.dto.request.RegisterUserRequest;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public abstract class BaseTest {

    protected RequestSpecification requestSpec;
    protected AuthApi authApi;
    protected UserApi userApi;
    protected IngredientsApi ingredientsApi;
    protected OrdersApi ordersApi;
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        RestAssured.basePath = "/api";

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();

        authApi = new AuthApi(requestSpec);
        userApi = new UserApi(requestSpec);
        ingredientsApi = new IngredientsApi(requestSpec);
        ordersApi = new OrdersApi(requestSpec);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }

    protected void registerAndAuthorizeUser(RegisterUserRequest user) {
        accessToken = authApi.registerUser(user)
                .then()
                .extract()
                .path("accessToken");
    }

    protected LoginUserRequest buildLoginRequest(String email, String password) {
        return new LoginUserRequest(email, password);
    }

    protected OrderRequest buildOrderRequest(List<String> ingredientIds) {
        return new OrderRequest(ingredientIds);
    }

    protected List<String> getIngredientIds(int count) {
        List<String> allIds = ingredientsApi.getIngredients()
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getList("data._id");

        if (allIds == null || allIds.size() < count) {
            throw new IllegalStateException("Недостаточно ингредиентов для теста");
        }

        return new ArrayList<>(allIds.subList(0, count));
    }
}
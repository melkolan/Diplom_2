package praktikum.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.constants.Endpoints;
import praktikum.dto.request.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrdersApi {

    private final RequestSpecification requestSpec;

    public OrdersApi(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Создать заказ без авторизации")
    public Response createOrder(OrderRequest request) {
        return given()
                .spec(requestSpec)
                .body(request)
                .when()
                .post(Endpoints.ORDERS);
    }

    @Step("Создать заказ с авторизацией")
    public Response createOrderAuthorized(OrderRequest request, String accessToken) {
        return given()
                .spec(requestSpec)
                .header("Authorization", accessToken)
                .body(request)
                .when()
                .post(Endpoints.ORDERS);
    }
}
package ru.netology.bank;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUp() {
        System.out.println("=== Starting API Tests ===");
        System.out.println("Base URI: http://localhost:9999");
        System.out.println("Endpoint: /api/system/users");
        System.out.println("=============================");
    }

    @Test
    void shouldRegisterActiveUser() {
        RegistrationDto user = DataGenerator.createRandomUser();
        String jsonBody = new com.google.gson.Gson().toJson(user);

        System.out.println("Test: Create active user");
        System.out.println("Request body: " + jsonBody);

        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200) // Попробуйте изменить на 201, если сервер возвращает Created
                .body("login", equalTo(user.getLogin()))
                .body("password", equalTo(user.getPassword()))
                .body("status", equalTo(user.getStatus()));
    }

    @Test
    void shouldRegisterBlockedUser() {
        RegistrationDto user = DataGenerator.createRandomUserWithStatus("blocked");
        String jsonBody = new com.google.gson.Gson().toJson(user);

        System.out.println("Test: Create blocked user");
        System.out.println("Request body: " + jsonBody);

        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200)
                .body("status", equalTo("blocked"));
    }

    @Test
    void shouldOverwriteExistingUser() {
        // Создаем пользователя
        RegistrationDto user = DataGenerator.createRandomUser();
        System.out.println("First user: " + user.getLogin());
        DataGenerator.registerUser(user);

        // Создаем нового пользователя с тем же логином
        RegistrationDto overwrittenUser = new RegistrationDto(
                user.getLogin(),
                "new_password",
                "blocked"
        );
        String jsonBody = new com.google.gson.Gson().toJson(overwrittenUser);

        System.out.println("Overwrite user: " + user.getLogin());
        System.out.println("New data: " + jsonBody);

        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200)
                .body("login", equalTo(user.getLogin()))
                .body("password", equalTo("new_password"))
                .body("status", equalTo("blocked"));
    }

    @Test
    void shouldRegisterMultipleUsers() {
        System.out.println("Test: Create multiple users");

        RegistrationDto user1 = DataGenerator.createRandomUser();
        RegistrationDto user2 = DataGenerator.createRandomUser();
        RegistrationDto user3 = DataGenerator.createRandomUser();

        System.out.println("User1: " + user1.getLogin());
        System.out.println("User2: " + user2.getLogin());
        System.out.println("User3: " + user3.getLogin());

        DataGenerator.registerUser(user1);
        DataGenerator.registerUser(user2);
        DataGenerator.registerUser(user3);

        System.out.println("All users created successfully");
    }

    @Test
    void shouldNotRegisterWithEmptyLogin() {
        RegistrationDto user = DataGenerator.createUserWithInvalidLogin();
        String jsonBody = new com.google.gson.Gson().toJson(user);

        System.out.println("Test: Empty login");
        System.out.println("Request body: " + jsonBody);

        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                // Если сервер возвращает 200, то это баг - создаем issue
                // Если возвращает 400 или 500 - это ожидаемо для невалидных данных
                .statusCode(400);
    }

    @Test
    void shouldNotRegisterWithEmptyPassword() {
        RegistrationDto user = DataGenerator.createUserWithInvalidPassword();
        String jsonBody = new com.google.gson.Gson().toJson(user);

        System.out.println("Test: Empty password");
        System.out.println("Request body: " + jsonBody);

        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldHandleInvalidJson() {
        String invalidJson = "{\"login\": \"test\"}"; // Неполный JSON - нет password и status

        System.out.println("Test: Invalid JSON (missing fields)");
        System.out.println("Request body: " + invalidJson);

        given()
                .spec(requestSpec)
                .body(invalidJson)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldHandleEmptyBody() {
        System.out.println("Test: Empty body");

        given()
                .spec(requestSpec)
                .body("")
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(400);
    }
}
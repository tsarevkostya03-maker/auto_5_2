package ru.netology.bank;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class DebugTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    void debugCreateUser() {
        RegistrationDto user = DataGenerator.createRandomUser();
        String jsonBody = new com.google.gson.Gson().toJson(user);

        System.out.println("=== DEBUG: Creating user ===");
        System.out.println("Login: " + user.getLogin());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Status: " + user.getStatus());
        System.out.println("JSON: " + jsonBody);
        System.out.println("=============================");

        String response = given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .extract()
                .response()
                .asString();

        System.out.println("Response: " + response);
        System.out.println("Status code: " + given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .extract()
                .statusCode());
    }

    @Test
    void debugInvalidLogin() {
        RegistrationDto user = DataGenerator.createUserWithInvalidLogin();
        String jsonBody = new com.google.gson.Gson().toJson(user);

        System.out.println("=== DEBUG: Empty login ===");
        System.out.println("JSON: " + jsonBody);
        System.out.println("============================");

        int statusCode = given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .extract()
                .statusCode();

        String response = given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .extract()
                .response()
                .asString();

        System.out.println("Status code: " + statusCode);
        System.out.println("Response: " + response);
    }

    @Test
    void debugEmptyBody() {
        System.out.println("=== DEBUG: Empty body ===");

        int statusCode = given()
                .spec(requestSpec)
                .body("")
                .when()
                .post("/api/system/users")
                .then()
                .extract()
                .statusCode();

        String response = given()
                .spec(requestSpec)
                .body("")
                .when()
                .post("/api/system/users")
                .then()
                .extract()
                .response()
                .asString();

        System.out.println("Status code: " + statusCode);
        System.out.println("Response: " + response);
    }
}
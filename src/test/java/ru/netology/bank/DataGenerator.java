package ru.netology.bank;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final Gson gson = new Gson();

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static RegistrationDto createRandomUser() {
        String login = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "pass_" + UUID.randomUUID().toString().substring(0, 8);
        return new RegistrationDto(login, password, "active");
    }

    public static RegistrationDto createRandomUserWithStatus(String status) {
        String login = "user_" + UUID.randomUUID().toString().substring(0, 8);
        String password = "pass_" + UUID.randomUUID().toString().substring(0, 8);
        return new RegistrationDto(login, password, status);
    }

    public static RegistrationDto createUserWithInvalidLogin() {
        return new RegistrationDto("", "password123", "active");
    }

    public static RegistrationDto createUserWithInvalidPassword() {
        return new RegistrationDto("valid_user", "", "active");
    }

    public static void registerUser(RegistrationDto user) {
        String jsonBody = gson.toJson(user);

        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
}
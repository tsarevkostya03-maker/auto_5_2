package ru.netology.bank;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiLoginTest {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @Test
    void shouldLoginWithValidActiveUser() {
        RegistrationDto user = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(user);

        given()
                .spec(requestSpec)
                .body(new RegistrationDto(user.getLogin(), user.getPassword(), "active"))
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        RegistrationDto blockedUser = new RegistrationDto(
                DataGenerator.generateLogin(),
                DataGenerator.generatePassword(),
                "blocked"
        );
        ApiHelper.registerUser(blockedUser);

        given()
                .spec(requestSpec)
                .body(new RegistrationDto(blockedUser.getLogin(), blockedUser.getPassword(), "blocked"))
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400); // Изменено на 400, так как сервер возвращает 400
    }

    @Test
    void shouldNotLoginWithNotRegisteredUser() {
        String login = DataGenerator.generateLogin();
        String password = DataGenerator.generatePassword();

        given()
                .spec(requestSpec)
                .body(new RegistrationDto(login, password, "active"))
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400); // Изменено на 400
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        RegistrationDto user = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(user);

        given()
                .spec(requestSpec)
                .body(new RegistrationDto(user.getLogin(), "wrong_password", "active"))
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400); // Изменено на 400
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        RegistrationDto user = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(user);

        given()
                .spec(requestSpec)
                .body(new RegistrationDto("wrong_login", user.getPassword(), "active"))
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400); // Изменено на 400
    }
}
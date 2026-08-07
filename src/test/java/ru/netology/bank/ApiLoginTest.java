package ru.netology.bank;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class ApiLoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldLoginWithValidActiveUser() {
        RegistrationDto validUser = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id='login'] input").setValue(validUser.getLogin());
        $("[data-test-id='password'] input").setValue(validUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").shouldNot(Condition.exist);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        RegistrationDto blockedUser = DataGenerator.generateBlockedUser();
        ApiHelper.registerUser(blockedUser);

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }

    @Test
    void shouldNotLoginWithNotRegisteredUser() {
        String login = DataGenerator.generateLogin();
        String password = DataGenerator.generatePassword();

        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        RegistrationDto validUser = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id='login'] input").setValue(validUser.getLogin());
        $("[data-test-id='password'] input").setValue("wrong_password");
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        RegistrationDto validUser = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(validUser);

        $("[data-test-id='login'] input").setValue("wrong_login");
        $("[data-test-id='password'] input").setValue(validUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка!"));
    }
}
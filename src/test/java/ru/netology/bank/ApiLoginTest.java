package ru.netology.bank;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class ApiLoginTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.timeout = 10000;
        // Открываем страницу входа
        open("http://localhost:9999/");
    }

    @Test
    void shouldLoginWithValidActiveUser() {
        // 1. Регистрируем активного пользователя через API
        RegistrationDto validUser = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(validUser);

        // 2. Заполняем форму входа на UI
        $("input[name='login']").setValue(validUser.getLogin());
        $("input[name='password']").setValue(validUser.getPassword());
        $("button[type='submit']").click();

        // 3. Проверяем успешный вход (например, наличие элемента дашборда)
        $("[data-test-id='dashboard']").shouldBe(Condition.visible);
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        // 1. Регистрируем заблокированного пользователя
        RegistrationDto blockedUser = DataGenerator.generateBlockedUser();
        ApiHelper.registerUser(blockedUser);

        // 2. Заполняем форму входа на UI
        $("input[name='login']").setValue(blockedUser.getLogin());
        $("input[name='password']").setValue(blockedUser.getPassword());
        $("button[type='submit']").click();

        // 3. Проверяем сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    void shouldNotLoginWithNotRegisteredUser() {
        // 1. Используем несуществующие данные
        String login = DataGenerator.generateLogin();
        String password = DataGenerator.generatePassword();

        // 2. Заполняем форму входа на UI
        $("input[name='login']").setValue(login);
        $("input[name='password']").setValue(password);
        $("button[type='submit']").click();

        // 3. Проверяем сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Неверный логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongPassword() {
        // 1. Регистрируем активного пользователя
        RegistrationDto validUser = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(validUser);

        // 2. Заполняем форму входа на UI с неправильным паролем
        $("input[name='login']").setValue(validUser.getLogin());
        $("input[name='password']").setValue("wrong_password");
        $("button[type='submit']").click();

        // 3. Проверяем сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Неверный логин или пароль"));
    }

    @Test
    void shouldNotLoginWithWrongLogin() {
        // 1. Регистрируем активного пользователя
        RegistrationDto validUser = DataGenerator.generateRandomUser();
        ApiHelper.registerUser(validUser);

        // 2. Заполняем форму входа на UI с неправильным логином
        $("input[name='login']").setValue("wrong_login");
        $("input[name='password']").setValue(validUser.getPassword());
        $("button[type='submit']").click();

        // 3. Проверяем сообщение об ошибке
        $("[data-test-id='error-notification']").shouldBe(Condition.visible);
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Неверный логин или пароль"));
    }
}

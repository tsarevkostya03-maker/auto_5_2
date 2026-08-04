package ru.netology.bank;

import com.github.javafaker.Faker;
import java.util.Locale;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password(6, 10);
    }

    public static String generateStatus() {
        return "active";
    }

    public static RegistrationDto generateRandomUser() {
        return new RegistrationDto(generateLogin(), generatePassword(), generateStatus());
    }

    // Эти методы нужны для обратной совместимости, если старые тесты еще используются
    public static RegistrationDto createRandomUser() {
        return generateRandomUser();
    }

    public static RegistrationDto createRandomUserWithStatus(String status) {
        return new RegistrationDto(generateLogin(), generatePassword(), status);
    }

    public static RegistrationDto createUserWithInvalidLogin() {
        return new RegistrationDto("", generatePassword(), "active");
    }

    public static RegistrationDto createUserWithInvalidPassword() {
        return new RegistrationDto(generateLogin(), "", "active");
    }
}
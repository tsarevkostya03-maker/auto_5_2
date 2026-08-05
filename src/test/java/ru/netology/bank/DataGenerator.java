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

    public static RegistrationDto generateRandomUser() {
        return new RegistrationDto(generateLogin(), generatePassword(), "active");
    }

    public static RegistrationDto generateBlockedUser() {
        return new RegistrationDto(generateLogin(), generatePassword(), "blocked");
    }
}

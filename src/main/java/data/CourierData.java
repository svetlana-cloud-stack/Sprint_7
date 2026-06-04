package data;

import com.github.javafaker.Faker;

public class CourierData {
    public static final String BASE_URI="https://qa-scooter.praktikum-services.ru";

    static Faker user = new Faker();
    public static final String LOGIN = user.name().lastName() + user.regexify("[0-9]{5}");
    public static final String PASSWORD = user.regexify("[0-9]{4}");
    public static final String FIRSTNAME = user.name().firstName();
    public static final String CREATE_COURIER_PATH = "/api/v1/courier";
    public static final String LOGIN_COURIER_PATH = "/api/v1/courier/login";
}



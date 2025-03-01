package com.todo.config;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

public class RestAssuredConfig {
    public static void setup() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.baseURI = Configuration.getInstance()
                .getProperty("BASE_URL");
        RestAssured.port = Integer.parseInt(Configuration.getInstance()
                .getProperty("PORT"));
    }
}

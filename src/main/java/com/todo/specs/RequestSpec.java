package com.todo.specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class RequestSpec {


    private static RequestSpecBuilder baseSpecBuilder() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addFilters(List.of(
                new RequestLoggingFilter(), new
                        ResponseLoggingFilter(),
                new AllureRestAssured()));
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.setAccept(ContentType.JSON);
        return requestSpecBuilder;
    }

    public static RequestSpecification unAuthSpec() {
        return baseSpecBuilder().build();
    }

    public static RequestSpecification authSpec() {
        PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();
        preemptiveBasicAuthScheme.setUserName("admin");
        preemptiveBasicAuthScheme.setPassword("admin");
        return baseSpecBuilder()
                .setAuth(preemptiveBasicAuthScheme)
                .build();
    }

    public static RequestSpecification invalidAuthSpec() {
        PreemptiveBasicAuthScheme preemptiveBasicAuthScheme = new PreemptiveBasicAuthScheme();
        preemptiveBasicAuthScheme.setUserName("invalidUser");
        preemptiveBasicAuthScheme.setPassword("invalidUser");
        return baseSpecBuilder()
                .setAuth(preemptiveBasicAuthScheme)
                .build();
    }
}

package com.todo.specs.response;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import static org.hamcrest.Matchers.*;

public class IncorrectDataResponse {

    public ResponseSpecification sameId() {
        return new ResponseSpecBuilder().expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(is(emptyOrNullString()))
                .build();
    }

    public ResponseSpecification badRequest() {
        return new ResponseSpecBuilder().expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectContentType(ContentType.TEXT)
                .expectBody(notNullValue())
                .build();
    }

    public ResponseSpecification notFound() {
        return new ResponseSpecBuilder().expectStatusCode(HttpStatus.SC_NOT_FOUND)
                .expectBody(is(emptyOrNullString()))
                .build();
    }

    public ResponseSpecification unAuthorized() {
        return new ResponseSpecBuilder().expectStatusCode(HttpStatus.SC_UNAUTHORIZED)
                .expectBody(is(emptyOrNullString()))
                .build();
    }
}

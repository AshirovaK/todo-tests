package com.todo.requests;

import com.todo.requests.facades.TodoRequester;
import com.todo.specs.request.RequestSpec;

public class TodoRequesterFactory {
    public static TodoRequester createRequester(AuthType authType) {
        switch (authType) {
            case AUTHENTICATED:
                return new TodoRequester(RequestSpec.authSpec());
            case UNAUTHENTICATED:
                return new TodoRequester(RequestSpec.unAuthSpec());
            case INVALID_AUTH:
                return new TodoRequester(RequestSpec.invalidAuthSpec());
            default:
                throw new IllegalArgumentException("Unknown AuthType");
        }
    }
}
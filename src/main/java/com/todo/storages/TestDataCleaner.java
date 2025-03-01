package com.todo.storages;

import com.todo.requests.TodoRequest;
import com.todo.specs.request.RequestSpec;

public class TestDataCleaner {
    public static void clean() {
        TestDataStorage.getInstance()
                .getStorage()
                .forEach((id, todo) ->
                        new TodoRequest(RequestSpec.authSpec()).delete(id)
                );
        TestDataStorage.getInstance()
                .cleanInstance();
    }
}

package com.todo.storages;

public class TestDataStorage {

    private static TestDataStorage instance;

    private TestDataStorage(){

    }

    public static  TestDataStorage getInstance(){
        if (instance == null) {
            instance = new TestDataStorage();
        }
        return instance;
    }
}

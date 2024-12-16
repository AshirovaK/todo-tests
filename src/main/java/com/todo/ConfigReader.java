package com.todo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static ConfigReader instance;
    private static Properties properties;
    static String propertyFilePath = "config.properties";
    static String BASE_URI = "BASE_URI";
    static String PORT = "PORT";

    private ConfigReader() {
        FileInputStream fis;
        try {
            fis = new FileInputStream(propertyFilePath);
            properties = new Properties();
            try {
                properties.load(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Not found:" + propertyFilePath);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getBaseUrl() {
        String baseUrl = properties.getProperty(BASE_URI);
        if (baseUrl != null) return baseUrl;
        else throw new RuntimeException(BASE_URI + " not specified in the file:" + propertyFilePath);
    }

    public Integer getPort() {
        String port = properties.getProperty(PORT);
        if (port != null) return Integer.valueOf(port);
        else throw new RuntimeException(BASE_URI + " not specified in the file:" + propertyFilePath);
    }
}

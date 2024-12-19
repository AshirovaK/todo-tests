package com.todo.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static Configuration instance;
    private static final Properties properties = new Properties();

    private Configuration() {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("config.properties file not found in resources");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getProperty(final String key) {
        String config = properties.getProperty(key);
        if (config != null) return config;
        else throw new RuntimeException(key + " not specified in the config.properties");
    }

    public void setProperty(final String key, final String value) {
        properties.setProperty(key, value);
    }
}

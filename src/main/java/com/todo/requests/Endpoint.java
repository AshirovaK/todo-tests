package com.todo.requests;

import com.todo.config.Configuration;


public class Endpoint {

    private String endpoint;
    private String version_config;

    public Endpoint(String endpoint) {
        this.endpoint = endpoint;
        this.version_config = Configuration.getProperty("version");
    }

    public String build() {
        String finalEndpoint = endpoint;
        if (this.version_config != null) {
            finalEndpoint = finalEndpoint + version_config;
        }
        return finalEndpoint;
    }
}

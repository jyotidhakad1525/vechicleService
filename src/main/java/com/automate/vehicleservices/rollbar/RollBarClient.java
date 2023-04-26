package com.automate.vehicleservices.rollbar;

import org.springframework.stereotype.Component;

@Component
public class RollBarClient {

    private static final String ACCESS_TOKEN = "14db5f4f723b455ab07351439c34affd";

    public String getAccessToken() {
        return ACCESS_TOKEN;
    }
}

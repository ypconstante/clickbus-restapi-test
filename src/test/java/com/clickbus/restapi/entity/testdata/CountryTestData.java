package com.clickbus.restapi.entity.testdata;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.Country;

public class CountryTestData {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    public static Country get() {
        return new Country()
            .setName("México")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1));
    }
}

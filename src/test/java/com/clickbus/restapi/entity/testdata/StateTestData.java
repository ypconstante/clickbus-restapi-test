package com.clickbus.restapi.entity.testdata;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.State;

public class StateTestData {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    public static State get() {
        return new State()
            .setCountry(CountryTestData.get())
            .setName("São Paulo")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1));
    }
}

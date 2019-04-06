package com.clickbus.restapi.entity.testdata;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.City;

public class CityTestData {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    public static City get() {
        return new City()
            .setState(StateTestData.get())
            .setName("Joinville")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1));
    }
}

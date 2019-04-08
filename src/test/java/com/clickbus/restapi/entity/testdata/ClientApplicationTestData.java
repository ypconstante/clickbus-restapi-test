package com.clickbus.restapi.entity.testdata;

import java.time.LocalDateTime;
import java.util.List;

import com.clickbus.restapi.entity.ClientApplication;
import com.clickbus.restapi.entity.Place;

public class ClientApplicationTestData {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    public static ClientApplication get() {
        return new ClientApplication()
            .setName("Client Name")
            .setPublicName("Public")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1))
            .setPlaces(List.of(
                new Place().setId(4L),
                new Place().setId(5L),
                new Place().setId(6L),
                new Place().setId(7L)
            ));
    }
}

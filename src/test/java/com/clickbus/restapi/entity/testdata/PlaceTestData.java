package com.clickbus.restapi.entity.testdata;

import java.time.LocalDateTime;
import java.util.List;

import com.clickbus.restapi.entity.ClientApplication;
import com.clickbus.restapi.entity.Place;

public class PlaceTestData {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    public static Place get() {
        return new Place()
            .setCity(CityTestData.get())
            .setName("Terminal Central")
            .setTerminalName("Plataforma A")
            .setSlug("terminal-central")
            .setAddress("Rua 123")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1))
            .setClientApplications(List.of(
                new ClientApplication().setId(1L),
                new ClientApplication().setId(2L),
                new ClientApplication().setId(4L)
            ));
    }
}

package com.clickbus.restapi.web.dto;

import java.util.Collection;
import java.util.Collections;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("Place")
public class PlaceDto {
    private Long id;
    private String name;
    private String slug;
    private PlaceDto.City city;
    private PlaceDto.State state;
    private PlaceDto.Country country;
    private Collection<Long> clientIds;

    public Collection<Long> getClientIds() {
        if (this.clientIds == null) {
            this.clientIds = Collections.emptyList();
        }
        return this.clientIds;
    }

    @Data
    public static class City {
        private String name;
    }

    @Data
    public static class State {
        private String name;
    }

    @Data
    public static class Country {
        private String name;
    }
}

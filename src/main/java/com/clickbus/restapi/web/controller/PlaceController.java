package com.clickbus.restapi.web.controller;

import java.util.Collection;

import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.service.api.PlaceService;
import com.clickbus.restapi.web.convert.PlaceConvert;
import com.clickbus.restapi.web.dto.PlaceDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/places",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceConvert placeConvert;

    public PlaceController(PlaceService placeService, PlaceConvert placeConvert) {
        this.placeService = placeService;
        this.placeConvert = placeConvert;
    }

    @GetMapping
    public Collection<PlaceDto> findAll() {
        Collection<Place> items = this.placeService.findAll();
        return this.placeConvert.toDto(items);
    }
}

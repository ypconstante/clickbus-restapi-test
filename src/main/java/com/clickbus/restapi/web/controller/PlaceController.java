package com.clickbus.restapi.web.controller;

import java.util.Collection;
import java.util.Optional;

import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.service.api.PlaceService;
import com.clickbus.restapi.web.convert.PlaceConvert;
import com.clickbus.restapi.web.dto.PlaceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/places",
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
@Api(tags = "places", description = "API to find places")
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceConvert placeConvert;

    public PlaceController(PlaceService placeService, PlaceConvert placeConvert) {
        this.placeService = placeService;
        this.placeConvert = placeConvert;
    }

    @GetMapping
    @ApiOperation(value = "List places")
    public Collection<PlaceDto> findAll(
        @ApiParam(value = "Filter by 'slug' containing value")
        @RequestParam(name = "slug", required = false)
            String slug) {
        Collection<Place> items = this.placeService.findAllBySlugContaining(slug);
        return this.placeConvert.toDto(items);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Get place by id")
    public ResponseEntity<PlaceDto> findById(@PathVariable(name = "id") Long id) {
        Optional<PlaceDto> item = this.placeService.findById(id)
            .map(this.placeConvert::toDto);
        return ResponseEntity.of(item);
    }
}

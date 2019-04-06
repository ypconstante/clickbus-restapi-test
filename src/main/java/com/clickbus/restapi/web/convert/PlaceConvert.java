package com.clickbus.restapi.web.convert;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.clickbus.restapi.entity.City;
import com.clickbus.restapi.entity.ClientApplication;
import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.entity.State;
import com.clickbus.restapi.web.dto.PlaceDto;
import org.springframework.stereotype.Component;

@Component
public class PlaceConvert {
    public Collection<PlaceDto> toDto(Collection<Place> places) {
        return places.stream().map(this::toDto).collect(Collectors.toList());
    }

    public PlaceDto toDto(Place place) {
        if (place == null) {
            return null;
        }
        return new PlaceDto()
            .setId(place.getId())
            .setName(place.getName())
            .setSlug(place.getSlug())
            .setCity(toDtoCity(place))
            .setState(toDtoState(place))
            .setCountry(toDtoCountry(place))
            .setClientIds(toDtoClients(place));
    }

    private PlaceDto.City toDtoCity(Place place) {
        return Optional
            .ofNullable(place.getCity())
            .map(it ->
                new PlaceDto.City()
                    .setName(it.getName()))
            .orElse(null);
    }

    private PlaceDto.State toDtoState(Place place) {
        return Optional
            .ofNullable(place.getCity())
            .map(City::getState)
            .map(it ->
                new PlaceDto.State()
                    .setName(it.getName()))
            .orElse(null);
    }

    private PlaceDto.Country toDtoCountry(Place place) {
        return Optional
            .ofNullable(place.getCity())
            .map(City::getState)
            .map(State::getCountry)
            .map(it ->
                new PlaceDto.Country()
                    .setName(it.getName()))
            .orElse(null);
    }

    private Collection<Long> toDtoClients(Place place) {
        return place
            .getClientApplications()
            .stream()
            .map(ClientApplication::getId)
            .collect(Collectors.toList());
    }

}

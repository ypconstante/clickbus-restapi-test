package com.clickbus.service.service.mapper;

import com.clickbus.service.domain.*;
import com.clickbus.service.service.dto.PlaceDTO;
import com.clickbus.service.service.dto.PlaceSimpleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Place and its DTO PlaceDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ClientApplicationMapper.class, CityMapper.class})
public interface PlaceMapper extends EntityMapper<PlaceDTO, Place> {

    @Mapping(source = "city.id", target = "cityId")
    PlaceDTO toDto(Place place);
    
    PlaceSimpleDTO toSimpleDto(Place place);

    @Mapping(source = "cityId", target = "city")
    Place toEntity(PlaceDTO placeDTO);
    
    default Place fromId(Long id) {
        if (id == null) {
            return null;
        }
        Place place = new Place();
        place.setId(id);
        return place;
    }
}

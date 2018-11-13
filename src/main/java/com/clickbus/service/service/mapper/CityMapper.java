package com.clickbus.service.service.mapper;

import com.clickbus.service.domain.*;
import com.clickbus.service.service.dto.CityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity City and its DTO CityDTO.
 */
@Mapper(componentModel = "spring", uses = {StateMapper.class})
public interface CityMapper extends EntityMapper<CityDTO, City> {

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    CityDTO toDto(City city);

    @Mapping(source = "stateId", target = "state")
    City toEntity(CityDTO cityDTO);

    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }
}

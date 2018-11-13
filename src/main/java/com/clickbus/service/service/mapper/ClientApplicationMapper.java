package com.clickbus.service.service.mapper;

import com.clickbus.service.domain.*;
import com.clickbus.service.service.dto.ClientApplicationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ClientApplication and its DTO ClientApplicationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientApplicationMapper extends EntityMapper<ClientApplicationDTO, ClientApplication> {


    @Mapping(target = "places", ignore = true)
    ClientApplication toEntity(ClientApplicationDTO clientApplicationDTO);

    default ClientApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setId(id);
        return clientApplication;
    }
}

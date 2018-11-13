package com.clickbus.service.service.dto.projections;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A DTO for the Place entity.
 */
@JsonIgnoreProperties("clients")
@JsonPropertyOrder({"id", "name", "slug", "city", "state", "country", "clientIds"})
public interface PlaceDetailsDTO extends PlaceProjectionsDTO {

    public Long getId();

    public String getName();

    public String getSlug();

    public CityID getCity();
    
    @Value("#{target.city.state}")
    public StateID getState();
    
    @Value("#{target.city.state.country}")
    public CountryID getCountry();
    
    @Value("#{target.clientApplications}")
    public Set<ClientID> getClients();
       
    default Set<Long> getClientIds() {
        return getClients().stream().map(ClientID::getId).collect(Collectors.toSet());
    }
    
    public interface CityID {
    	public String getName();
    }
    
    public interface StateID {
    	public String getName();
    }
    
    public interface CountryID {
    	public String getName();
    }
    
    public interface ClientID {
    	public Long getId();
    }

}

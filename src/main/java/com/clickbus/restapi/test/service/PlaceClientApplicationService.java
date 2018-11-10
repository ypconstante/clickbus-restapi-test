package com.clickbus.restapi.test.service;

import java.util.List;

import com.clickbus.restapi.test.entity.PlaceClientApplication;

public interface PlaceClientApplicationService {
	
	PlaceClientApplication save(PlaceClientApplication c);
	
	List<PlaceClientApplication> findByPlaceId(Long id);
}

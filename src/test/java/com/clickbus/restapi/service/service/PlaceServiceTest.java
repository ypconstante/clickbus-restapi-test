package com.clickbus.restapi.service.service;

import com.clickbus.restapi.repository.PlaceRepository;
import com.clickbus.restapi.service.api.PlaceService;
import com.clickbus.restapi.service.impl.PlaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PlaceServiceTest {
    PlaceService placeService;
    @Mock PlaceRepository placeRepository;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
        this.placeService = new PlaceServiceImpl(this.placeRepository);
    }

    @Nested
    class FindAllBySlugContaining {
        @Test
        void lowerCaseSlug() {
            PlaceServiceTest.this.placeService.findAllBySlugContaining("some-slug");
            Mockito.verify(PlaceServiceTest.this.placeRepository, Mockito.never()).findAll();
            Mockito.verify(PlaceServiceTest.this.placeRepository).findAllBySlugContaining("some-slug");
        }

        @Test
        void upperCaseSlug() {
            PlaceServiceTest.this.placeService.findAllBySlugContaining("UPPER-SLUG");
            Mockito.verify(PlaceServiceTest.this.placeRepository, Mockito.never()).findAll();
            Mockito.verify(PlaceServiceTest.this.placeRepository).findAllBySlugContaining("upper-slug");
        }

        @Test
        void nullSlug() {
            PlaceServiceTest.this.placeService.findAllBySlugContaining(null);
            Mockito.verify(PlaceServiceTest.this.placeRepository).findAll();
            Mockito.verify(PlaceServiceTest.this.placeRepository, Mockito.never()).findAllBySlugContaining(Mockito.any());
        }

        @Test
        void emptySlug() {
            PlaceServiceTest.this.placeService.findAllBySlugContaining("");
            Mockito.verify(PlaceServiceTest.this.placeRepository).findAll();
            Mockito.verify(PlaceServiceTest.this.placeRepository, Mockito.never()).findAllBySlugContaining(Mockito.any());
        }
    }
}

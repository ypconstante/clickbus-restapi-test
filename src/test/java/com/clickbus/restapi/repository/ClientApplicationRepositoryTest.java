package com.clickbus.restapi.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.clickbus.restapi.entity.ClientApplication;
import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.entity.testdata.ClientApplicationTestData;
import com.clickbus.restapi.test.AppRepositoryTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

class ClientApplicationRepositoryTest extends AppRepositoryTestBootstrapper {
    @Autowired
    private ClientApplicationRepository clientApplicationRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void save() {
        ClientApplication item = this.clientApplicationRepository
            .save(getNewClientApplication());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    void saveAndFind() {
        LocalDateTime timeBeforeSave = LocalDateTime.now();
        Long id = this.clientApplicationRepository
            .save(getNewClientApplication()).getId();

        ClientApplication item = this.clientApplicationRepository.findById(id)
            .orElseThrow(IllegalStateException::new);

        ClientApplication expected = getNewClientApplication()
            .setId(id)
            .setCreatedAt(item.getCreatedAt())
            .setUpdatedAt(item.getUpdatedAt());

        assertThat(item).isEqualTo(expected);
        assertThat(item.getCreatedAt())
            .isAfterOrEqualTo(timeBeforeSave)
            .isBeforeOrEqualTo(item.getUpdatedAt());
        assertThat(item.getPlaces())
            .containsExactlyInAnyOrderElementsOf(expected.getPlaces());
    }

    @Test
    void findPreviousData() {
        ClientApplication item = this.clientApplicationRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo("First Client Ltda");
        assertThat(item.getPublicName()).isEqualTo("The Client");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 10, 20, 10, 11, 12));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2019, 1, 5, 17, 59, 59));
        assertThat(item.getPlaces()).extracting(Place::getId)
            .hasSize(4)
            .containsExactlyInAnyOrder(1L, 2L, 4L, 5L);
    }

    private ClientApplication getNewClientApplication() {
        ClientApplication clientApplication = ClientApplicationTestData.get();

        List<Place> places = this.placeRepository.findAllById(
            clientApplication.getPlaces().stream()
                .map(Place::getId)
                .collect(Collectors.toList())
        );

        return clientApplication
            .setPlaces(places);
    }
}

package com.clickbus.restapi.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.clickbus.restapi.entity.City;
import com.clickbus.restapi.entity.ClientApplication;
import com.clickbus.restapi.entity.Place;
import com.clickbus.restapi.entity.testdata.PlaceTestData;
import com.clickbus.restapi.test.AppRepositoryTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PlaceRepositoryTest extends AppRepositoryTestBootstrapper {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ClientApplicationRepository clientApplicationRepository;

    @Test
    void save() {
        Place item = this.placeRepository.save(getNewPlace());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    void saveDuplicatedSlug() {
        this.placeRepository.save(getNewPlace());
        Throwable throwable = catchThrowable(() -> this.placeRepository.save(getNewPlace()));
        assertThat(throwable)
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("PLACE_SLUG_AK");
    }

    @Test
    void saveAndFind() {
        LocalDateTime timeBeforeSave = LocalDateTime.now();
        Long id = this.placeRepository.save(getNewPlace()).getId();

        Place item = this.placeRepository.findById(id)
            .orElseThrow(IllegalStateException::new);

        Place expected = getNewPlace()
            .setId(id)
            .setCreatedAt(item.getCreatedAt())
            .setUpdatedAt(item.getUpdatedAt());

        assertThat(item).isEqualTo(expected);
        assertThat(item.getCreatedAt())
            .isAfterOrEqualTo(timeBeforeSave)
            .isBeforeOrEqualTo(item.getUpdatedAt());
        assertThat(item.getClientApplications())
            .containsExactlyInAnyOrderElementsOf(expected.getClientApplications());
    }

    @Test
    void findPreviousData() {
        Place item = this.placeRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getCity()).isNotNull();
        assertThat(item.getCity().getName()).isEqualTo("Floripa");
        assertThat(item.getName()).isEqualTo("Terminal Rita Maria");
        assertThat(item.getTerminalName()).isEqualTo("A01");
        assertThat(item.getSlug()).isEqualTo("terminal-rita-maria");
        assertThat(item.getAddress()).isEqualTo("Rua do Terminal");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 28, 42));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 28, 43));
        assertThat(item.getClientApplications()).extracting(ClientApplication::getId)
            .hasSize(3)
            .containsExactlyInAnyOrder(1L, 2L, 4L);
    }

    @Test
    void findAllBySlugContaining() {
        Place place123 = this.placeRepository.save(getNewPlace().setSlug("some-place-123"));
        Place place124 = this.placeRepository.save(getNewPlace().setSlug("some-place-124"));
        assertThat(this.placeRepository.findAllBySlugContaining("some-place-12"))
            .containsExactlyInAnyOrder(place123, place124);
        assertThat(this.placeRepository.findAllBySlugContaining("some-place-123"))
            .containsExactlyInAnyOrder(place123);
        assertThat(this.placeRepository.findAllBySlugContaining("-"))
            .hasSize(18);
    }

    private Place getNewPlace() {
        Place place = PlaceTestData.get();

        City city = this.cityRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        List<ClientApplication> clientApplications = this.clientApplicationRepository.findAllById(
            place.getClientApplications().stream()
                .map(ClientApplication::getId)
                .collect(Collectors.toList())
        );
        return place
            .setCity(city)
            .setClientApplications(clientApplications);
    }
}

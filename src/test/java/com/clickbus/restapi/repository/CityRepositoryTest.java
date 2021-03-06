package com.clickbus.restapi.repository;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.City;
import com.clickbus.restapi.entity.State;
import com.clickbus.restapi.entity.testdata.CityTestData;
import com.clickbus.restapi.test.AppRepositoryTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

class CityRepositoryTest extends AppRepositoryTestBootstrapper {
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CityRepository cityRepository;

    @Test
    void save() {
        City item = this.cityRepository.save(getNewCity());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    void saveAndFind() {
        LocalDateTime timeBeforeSave = LocalDateTime.now();
        Long id = this.cityRepository.save(getNewCity()).getId();

        City item = this.cityRepository.findById(id)
            .orElseThrow(IllegalStateException::new);

        City expected = getNewCity()
            .setId(id)
            .setCreatedAt(item.getCreatedAt())
            .setUpdatedAt(item.getUpdatedAt());

        assertThat(item).isEqualTo(expected);
        assertThat(item.getCreatedAt())
            .isAfterOrEqualTo(timeBeforeSave)
            .isBeforeOrEqualTo(item.getUpdatedAt());
    }

    @Test
    void findPreviousData() {
        City item = this.cityRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getState()).isNotNull();
        assertThat(item.getState().getName()).isEqualTo("Santa Catarina");
        assertThat(item.getName()).isEqualTo("Floripa");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 23, 1));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 23, 2));
    }

    private City getNewCity() {
        State state = this.stateRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        return CityTestData.get()
            .setState(state);
    }
}

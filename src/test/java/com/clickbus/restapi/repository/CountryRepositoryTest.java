package com.clickbus.restapi.repository;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.Country;
import com.clickbus.restapi.entity.testdata.CountryTestData;
import com.clickbus.restapi.test.AppRepositoryTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

class CountryRepositoryTest extends AppRepositoryTestBootstrapper {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    void save() {
        Country item = this.countryRepository
            .save(CountryTestData.get());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    void saveAndFind() {
        LocalDateTime timeBeforeSave = LocalDateTime.now();
        Long id = this.countryRepository
            .save(CountryTestData.get()).getId();

        Country item = this.countryRepository.findById(id)
            .orElseThrow(IllegalStateException::new);

        Country expected = CountryTestData.get()
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
        Country item = this.countryRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo("Brasil");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 1, 8, 1, 13));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 1, 8, 1, 14));
    }
}

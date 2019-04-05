package com.clickbus.restapi.repository;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CountryRepositoryTest {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void save() {
        Country item = this.countryRepository
            .save(getNewCountry());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    public void saveAndFind() {
        Long id = this.countryRepository
            .save(getNewCountry()).getId();
        Country item = this.countryRepository.findById(id)
            .orElseThrow(IllegalStateException::new);
        item.setId(null);
        assertThat(item).isEqualTo(getNewCountry());
    }

    @Test
    public void findPreviousData() {
        Country item = this.countryRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo("Brasil");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 1, 8, 1, 13));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 1, 8, 1, 14));
    }

    public Country getNewCountry() {
        return new Country()
            .setName("México")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1));
    }
}
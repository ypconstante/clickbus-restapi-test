package com.clickbus.restapi.repository;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.Country;
import com.clickbus.restapi.entity.State;
import com.clickbus.restapi.entity.testdata.StateTestData;
import com.clickbus.restapi.test.AppRepositoryTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class StateRepositoryTest extends AppRepositoryTestBootstrapper {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StateRepository stateRepository;

    @Test
    public void save() {
        State item = this.stateRepository.save(getNewState());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    public void saveAndFind() {
        Long id = this.stateRepository.save(getNewState()).getId();
        State item = this.stateRepository.findById(id)
            .orElseThrow(IllegalStateException::new);
        item.setId(null);
        assertThat(item).isEqualTo(getNewState());
    }

    @Test
    public void findPreviousData() {
        State item = this.stateRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getCountry()).isNotNull();
        assertThat(item.getCountry().getName()).isEqualTo("Brasil");
        assertThat(item.getName()).isEqualTo("Santa Catarina");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 15, 16));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 15, 17));
    }

    private State getNewState() {
        Country country = this.countryRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        return StateTestData.get()
            .setCountry(country);
    }
}

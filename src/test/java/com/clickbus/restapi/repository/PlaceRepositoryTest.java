package com.clickbus.restapi.repository;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.City;
import com.clickbus.restapi.entity.Place;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PlaceRepositoryTest {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void save() {
        Place item = this.placeRepository.save(getNewPlace());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    public void saveDuplicatedSlug() {
        this.placeRepository.save(getNewPlace());
        Throwable throwable = catchThrowable(() -> this.placeRepository.save(getNewPlace()));
        assertThat(throwable)
            .isInstanceOf(DataIntegrityViolationException.class)
            .hasMessageContaining("PLACE_SLUG_AK");
    }

    @Test
    public void saveAndFind() {
        Long id = this.placeRepository.save(getNewPlace()).getId();
        Place item = this.placeRepository.findById(id)
            .orElseThrow(IllegalStateException::new);
        item.setId(null);
        assertThat(item).isEqualTo(getNewPlace());
    }

    @Test
    public void findPreviousData() {
        Place item = this.placeRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getCity()).isNotNull();
        assertThat(item.getCity().getName()).isEqualTo("Floripa");
        assertThat(item.getName()).isEqualTo("Terminal Rita Maria");
        assertThat(item.getTerminalName()).isEqualTo("A1");
        assertThat(item.getSlug()).isEqualTo("terminal-rita-maria");
        assertThat(item.getAddress()).isEqualTo("Rua do Terminal");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 28, 42));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2013, 8, 2, 14, 28, 43));
    }

    private Place getNewPlace() {
        City city = this.cityRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        return new Place()
            .setCity(city)
            .setName("Terminal Central")
            .setTerminalName("Plataforma A")
            .setSlug("terminal-central")
            .setAddress("Rua 123")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1));
    }
}

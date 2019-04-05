package com.clickbus.restapi.repository;

import java.time.LocalDateTime;

import com.clickbus.restapi.entity.ClientApplication;
import com.clickbus.restapi.test.AppRepositoryTestBootstrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class ClientApplicationRepositoryTest extends AppRepositoryTestBootstrapper {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();

    @Autowired
    private ClientApplicationRepository clientApplicationRepository;

    @Test
    public void save() {
        ClientApplication item = this.clientApplicationRepository
            .save(getNewClientApplication());
        assertThat(item.getId()).isGreaterThan(1);
    }

    @Test
    public void saveAndFind() {
        Long id = this.clientApplicationRepository
            .save(getNewClientApplication()).getId();
        ClientApplication item = this.clientApplicationRepository.findById(id)
            .orElseThrow(IllegalStateException::new);
        item.setId(null);
        assertThat(item).isEqualTo(getNewClientApplication());
    }

    @Test
    public void findPreviousData() {
        ClientApplication item = this.clientApplicationRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(item.getId()).isNotNull();
        assertThat(item.getName()).isEqualTo("First Client Ltda");
        assertThat(item.getPublicName()).isEqualTo("The Client");
        assertThat(item.getCreatedAt())
            .isEqualTo(LocalDateTime.of(2013, 10, 20, 10, 11, 12));
        assertThat(item.getUpdatedAt())
            .isEqualTo(LocalDateTime.of(2019, 1, 5, 17, 59, 59));
    }

    private ClientApplication getNewClientApplication() {
        return new ClientApplication()
            .setName("Client Name")
            .setPublicName("Public")
            .setCreatedAt(BASE_TIME)
            .setUpdatedAt(BASE_TIME.plusSeconds(1));
    }
}

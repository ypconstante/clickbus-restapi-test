package com.clickbus.restapi.repository;

import com.clickbus.restapi.entity.ClientApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientApplicationRepository extends JpaRepository<ClientApplication, Long> {
}

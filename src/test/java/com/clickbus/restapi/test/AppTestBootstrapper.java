package com.clickbus.restapi.test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = {
    "classpath:application.properties",
    "classpath:test.properties"
})
abstract class AppTestBootstrapper extends SpringBootTestContextBootstrapper {
}

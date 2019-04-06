package com.clickbus.restapi.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@TestPropertySource(locations = {
    "classpath:application.properties",
    "classpath:test.properties"
})
public class AppRepositoryTestBootstrapper extends SpringBootTestContextBootstrapper {
}

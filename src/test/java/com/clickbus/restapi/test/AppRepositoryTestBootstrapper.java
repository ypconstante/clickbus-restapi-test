package com.clickbus.restapi.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AppRepositoryTestBootstrapper extends SpringBootTestContextBootstrapper {
}

package com.clickbus.service.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.clickbus.service.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.clickbus.service.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            
            cm.createCache(com.clickbus.service.domain.Country.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.Country.class.getName() + ".states", jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.State.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.State.class.getName() + ".cities", jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.City.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.City.class.getName() + ".places", jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.Place.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.Place.class.getName() + ".clientApplications", jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.ClientApplication.class.getName(), jcacheConfiguration);
            cm.createCache(com.clickbus.service.domain.ClientApplication.class.getName() + ".places", jcacheConfiguration);
        };
    }
}

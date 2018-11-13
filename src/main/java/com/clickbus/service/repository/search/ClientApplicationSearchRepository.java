package com.clickbus.service.repository.search;

import com.clickbus.service.domain.ClientApplication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ClientApplication entity.
 */
public interface ClientApplicationSearchRepository extends ElasticsearchRepository<ClientApplication, Long> {
}

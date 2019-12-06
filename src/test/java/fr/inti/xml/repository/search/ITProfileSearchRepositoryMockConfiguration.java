package fr.inti.xml.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ITProfileSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ITProfileSearchRepositoryMockConfiguration {

    @MockBean
    private ITProfileSearchRepository mockITProfileSearchRepository;

}

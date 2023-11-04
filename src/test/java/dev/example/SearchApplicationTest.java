package dev.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import dev.example.services.Service;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
@Transactional
class SearchApplicationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ServiceClassifierAgent serviceClassifierAgent;

    @Autowired
    private ServiceFinderAgent serviceFinderAgent;

    @Test
    void initTest() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
        int indexSize = fullTextEntityManager.getSearchFactory()
            .getStatistics()
            .getNumberOfIndexedEntities(Service.class.getName());
        assertEquals(38, indexSize);
    }


    @Test
    void this_is_a_custom_test() {
        // Testing embeddings vs lucene
        interact(serviceClassifierAgent,serviceFinderAgent,"bigliografia");
        interact(serviceClassifierAgent,serviceFinderAgent,"bibliografía");
        interact(serviceClassifierAgent,serviceFinderAgent,"cafés");
    }
    
    private void interact(ServiceClassifierAgent serviceClassifierAgent, ServiceFinderAgent serviceFinderAgent, String userMessage) {
        System.out.println("==========================================================================================");
        System.out.println("[User]: " + userMessage);
        System.out.println("==========================================================================================");
        String agentAnswer0 = serviceClassifierAgent.getServiceNames(userMessage).toString();
        String agentAnswer1 = serviceFinderAgent.getServiceNames(userMessage).toString();
        String agentAnswer2 = serviceFinderAgent.getFuzzyServiceNames(userMessage).toString();
        String agentAnswer3 = serviceFinderAgent.getPhraseServiceNames(userMessage).toString();
        System.out.println("==========================================================================================");
        System.out.println("[AgentIA]: " + agentAnswer0);
        System.out.println("==========================================================================================");
        System.out.println("[AgentQuery]: " + agentAnswer1);
        System.out.println("==========================================================================================");
        System.out.println("[AgentFuzzy]: " + agentAnswer2);
        System.out.println("==========================================================================================");
        System.out.println("[AgentPhrase]: " + agentAnswer3);
        System.out.println("==========================================================================================");
    }

}

package dev.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        interact(serviceClassifierAgent,serviceFinderAgent,"libros");
        interact(serviceClassifierAgent,serviceFinderAgent,"novelas");
        interact(serviceClassifierAgent,serviceFinderAgent,"cafés");
        interact(serviceClassifierAgent,serviceFinderAgent,"almuerzo");
        interact(serviceClassifierAgent,serviceFinderAgent,"montaña");
        interact(serviceClassifierAgent,serviceFinderAgent,"playa");
        interact(serviceClassifierAgent,serviceFinderAgent,"almanaque");
        interact(serviceClassifierAgent,serviceFinderAgent,"llavero");
        interact(serviceClassifierAgent,serviceFinderAgent,"ayuda experta");
    }
    
    private void interact(ServiceClassifierAgent serviceClassifierAgent, ServiceFinderAgent serviceFinderAgent, String userMessage) {
        System.out.println("==========================================================================================");
        System.out.println("[User]: " + userMessage);
        System.out.println("==========================================================================================");
        String agentAnswerL6 = serviceClassifierAgent.getServiceNamesL6(userMessage).toString();
        String agentAnswerE5 = serviceClassifierAgent.getServiceNamesE5(userMessage).toString();
        String agentAnswerQ1 = serviceFinderAgent.getServiceNames(userMessage).toString();
        String agentAnswerF2 = serviceFinderAgent.getFuzzyServiceNames(userMessage).toString();
        String agentAnswerP3 = serviceFinderAgent.getPhraseServiceNames(userMessage).toString();
        System.out.println("==========================================================================================");
        System.out.println("[AgentIAL6]: " + agentAnswerL6);
        System.out.println("==========================================================================================");
        System.out.println("[AgentIAE5]: " + agentAnswerE5);
        System.out.println("==========================================================================================");
        System.out.println("[AgentQuery]: " + agentAnswerQ1);
        System.out.println("==========================================================================================");
        System.out.println("[AgentFuzzy]: " + agentAnswerF2);
        System.out.println("==========================================================================================");
        System.out.println("[AgentPhrase]: " + agentAnswerP3);
        System.out.println("==========================================================================================");
    }

}

package dev.example;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.jpa.FullTextQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.stereotype.Component;

import dev.example.services.Service;

@Component
public class ServiceFinderAgent {
    
    @PersistenceContext
    private EntityManager entityManager;

    public ServiceFinderAgent() {}

    public List<String> getServiceNames(String text) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        //fullTextEntityManager.createIndexer().startAndWait();
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory() 
            .buildQueryBuilder()
            .forEntity(Service.class)
            .get();        
        Query query = queryBuilder
            .keyword()
            .onFields("desc","name")
            .matching(text)
            .createQuery();
        FullTextQuery jpaQuery
            = fullTextEntityManager.createFullTextQuery(query, Service.class);
        return (List<String>) jpaQuery.getResultList().stream().map(s -> ((Service)s).name()).collect(Collectors.toList());
    }

    public List<String> getFuzzyServiceNames(String text) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        //fullTextEntityManager.createIndexer().startAndWait();
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory() 
            .buildQueryBuilder()
            .forEntity(Service.class)
            .get();        
        Query fuzzyQuery = queryBuilder
            .keyword()
            .fuzzy()
            .withEditDistanceUpTo(2)
            .withPrefixLength(0)
            .onFields("desc","name")
            .matching(text)
            .createQuery();            
        FullTextQuery jpaQuery
            = fullTextEntityManager.createFullTextQuery(fuzzyQuery, Service.class);
        return (List<String>) jpaQuery.getResultList().stream().map(s -> ((Service)s).name()).collect(Collectors.toList());
    }


    public List<String> getPhraseServiceNames(String text) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        //fullTextEntityManager.createIndexer().startAndWait();
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory() 
            .buildQueryBuilder()
            .forEntity(Service.class)
            .get();       
        Query phraseQuery = queryBuilder
            .phrase()
            .withSlop(1)
            .onField("desc")
            .andField("name")
            .sentence(text)
            .createQuery();
            
        FullTextQuery jpaQuery
            = fullTextEntityManager.createFullTextQuery(phraseQuery, Service.class);
        return (List<String>) jpaQuery.getResultList().stream().map(s -> ((Service)s).name()).collect(Collectors.toList());
    }

}

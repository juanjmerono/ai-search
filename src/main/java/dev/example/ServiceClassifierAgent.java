package dev.example;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.example.services.JPAServiceRepository;
import dev.example.services.Service;
import dev.langchain4j.model.embedding.EmbeddingModel;

@Component
public class ServiceClassifierAgent {
    
    private JPAServiceRepository jpaServiceRepository;
    private CustomServiceClassifierAgent customClassifier;

    public ServiceClassifierAgent(EmbeddingModel embeddingModel, JPAServiceRepository jpaServiceRepository) {
        this.jpaServiceRepository = jpaServiceRepository;
        customClassifier = new CustomServiceClassifierAgent(embeddingModel, 
            jpaServiceRepository.findAll().stream()
            .collect(Collectors.toMap(Service::id,Service::nameAndDescList)), 
            5, 0.601, 0.1);
    }


    public List<String> getServiceNames(String text) {
        List<String> services = customClassifier.classify(text);
        return services.stream().map(s -> jpaServiceRepository.findById(s).get().name()).collect(Collectors.toList());
    }

}

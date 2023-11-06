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
    private CustomServiceClassifierAgent customClassifierL6, customClassifierE5;

    public ServiceClassifierAgent(EmbeddingModel embeddingModelL6, EmbeddingModel embeddingModelE5, JPAServiceRepository jpaServiceRepository) {
        this.jpaServiceRepository = jpaServiceRepository;
        customClassifierL6 = new CustomServiceClassifierAgent(embeddingModelL6, 
            jpaServiceRepository.findAll().stream()
            .collect(Collectors.toMap(Service::id,Service::nameAndDescList)), 
            5, 0.601, 0.1);
        customClassifierE5 = new CustomServiceClassifierAgent(embeddingModelE5, 
            jpaServiceRepository.findAll().stream()
            .collect(Collectors.toMap(Service::id,Service::nameAndDescList)), 
            5, 0.601, 0.1);
    }


    public List<String> getServiceNamesL6(String text) {
        List<String> services = customClassifierL6.classify(text);
        return services.stream().map(s -> jpaServiceRepository.findById(s).get().name()).collect(Collectors.toList());
    }

    public List<String> getServiceNamesE5(String text) {
        List<String> services = customClassifierE5.classify(text);
        return services.stream().map(s -> jpaServiceRepository.findById(s).get().name()).collect(Collectors.toList());
    }

}

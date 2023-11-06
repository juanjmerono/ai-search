package dev.example;

import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.E5SmallV2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SearchApplication {

    /*@Bean
    ApplicationRunner applicationRunner(SentimentAnalyzer sentimentAnalyzer) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("[User]: ");
                String userRequest = scanner.nextLine();
                System.out.println("[Agent]: "+sentimentAnalyzer.analyzeSentimentOf(userRequest));
            }
        };
    }*/

    @Bean
    EmbeddingModel embeddingModelL6() {
        //return new HuggingFaceEmbeddingModel(apiKey,"sentence-transformers/all-MiniLM-L6-v2", true, Duration.ofSeconds(60));
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    EmbeddingModel embeddingModelE5() {
        //return new HuggingFaceEmbeddingModel(apiKey,"sentence-transformers/all-MiniLM-L6-v2", true, Duration.ofSeconds(60));
        return new E5SmallV2EmbeddingModel();
    }


    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

}

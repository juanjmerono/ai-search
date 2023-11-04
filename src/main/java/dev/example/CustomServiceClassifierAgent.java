package dev.example;

import static java.util.stream.Collectors.toList;
import static java.util.Comparator.comparingDouble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;
import dev.langchain4j.store.embedding.RelevanceScore;

public class CustomServiceClassifierAgent {

    private EmbeddingModel embeddingModel;
    private final Map<String, List<Embedding>> exampleEmbeddingsByLabel;
    private final int maxResults;
    private final double minScore;
    private final double meanToMaxScoreRatio;

    public CustomServiceClassifierAgent(EmbeddingModel embeddingModel,
                                        Map<String, ? extends Collection<String>> examplesByLabel,
                                        int maxResults,
                                        double minScore,
                                        double meanToMaxScoreRatio) {
        this.embeddingModel = embeddingModel;
        this.exampleEmbeddingsByLabel = new HashMap<>();
        examplesByLabel.forEach((label, examples) ->
                exampleEmbeddingsByLabel.put(label, examples.stream()
                        .map(example -> embeddingModel.embed(example).content())
                        .collect(toList()))
        );

        this.maxResults = maxResults;
        this.minScore = minScore;
        this.meanToMaxScoreRatio = meanToMaxScoreRatio;        
    }

    public List<String> classify(String text) {
        Embedding textEmbedding = embeddingModel.embed(text).content();

        List<LabelWithScore> labelsWithScores = new ArrayList<>();
        exampleEmbeddingsByLabel.forEach((label, exampleEmbeddings) -> {

            double meanScore = 0;
            double maxScore = 0;
            for (Embedding exampleEmbedding : exampleEmbeddings) {
                double cosineSimilarity = CosineSimilarity.between(textEmbedding, exampleEmbedding);
                double score = RelevanceScore.fromCosineSimilarity(cosineSimilarity);
                meanScore += score;
                maxScore = Math.max(score, maxScore);
            }
            meanScore /= exampleEmbeddings.size();

            labelsWithScores.add(new LabelWithScore(label, aggregatedScore(meanScore, maxScore)));
        });

        return labelsWithScores.stream()
                .filter(it -> it.score >= minScore)
                // sorting in descending order to return highest score first
                .sorted(comparingDouble(labelWithScore -> 1 - labelWithScore.score))
                .limit(maxResults)
                .map(it -> it.label)
                .collect(toList());        
    }

    private double aggregatedScore(double meanScore, double maxScore) {
        return (meanToMaxScoreRatio * meanScore) + ((1 - meanToMaxScoreRatio) * maxScore);
    }

    private class LabelWithScore {

        private final String label;
        private final double score;

        private LabelWithScore(String label, double score) {
            this.label = label;
            this.score = score;
        }
    }

}

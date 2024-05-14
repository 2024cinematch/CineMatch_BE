package com.example.softwareEngBE.logic;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

@Component
public class CosineSimilarity {
    private static final Logger logger = LoggerFactory.getLogger(CosineSimilarity.class);

    private static final Set<String> globalWordSet = new HashSet<>();

    // 전역 단어 집합 생성 (초기화는 어플리케이션 시작 시 한 번 수행)
    public static void initializeGlobalWordSet(List<String> allTitles) {
        for (String title : allTitles) {
            String[] words = title.toLowerCase().split("\\s+");
            for (String word : words) {
                globalWordSet.add(word);
            }
        }
    }

    public static double cosineSimilarity(String title1, String title2) {
        RealVector vector1 = titleToVector(title1);
        RealVector vector2 = titleToVector(title2);

        double dotProduct = vector1.dotProduct(vector2);
        double normProduct = vector1.getNorm() * vector2.getNorm();

        if (normProduct == 0) {
            logger.debug("Norm product is zero, titles: {} and {}", title1, title2);
            return 0.0;
        }
        double similarity = dotProduct / normProduct;
        logger.debug("Calculated cosine similarity between '{}' and '{}' is {}", title1, title2, similarity);
        return similarity;
    }

    public static RealVector titleToVector(String title) {
        Map<String, Integer> wordMap = new HashMap<>();
        String[] words = title.toLowerCase().split("\\s+");
        for (String word : words) {
            if (globalWordSet.contains(word)) { // 전역 단어 집합에 있는 단어만 고려
                wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
            }
        }

        double[] vector = new double[globalWordSet.size()];
        int i = 0;
        for (String word : globalWordSet) {
            vector[i++] = wordMap.getOrDefault(word, 0);
        }
        return new ArrayRealVector(vector);
    }

}

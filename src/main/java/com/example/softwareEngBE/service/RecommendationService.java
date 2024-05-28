package com.example.softwareEngBE.service;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    private final MoviesRepository moviesRepository;

    @Autowired
    public RecommendationService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public List<MoviesDto> recommendMoviesByTitle(String title) {
        try {
            Movies movie = moviesRepository.findByTitle(title);
            if (movie == null) {
                throw new IllegalArgumentException("Movie not found");
            }
            String genre = movie.getGenres();
            logger.info("Found movie: {} with genre: {}", title, genre);

            List<Movies> similarMovies = moviesRepository.findByGenresContainingIgnoreCase(genre);
            logger.info("Found {} similar movies", similarMovies.size());

            return similarMovies.stream()
                    .filter(m -> !m.getTitle().equals(title))
                    .sorted((m1, m2) -> Double.compare(
                            CosineSimilarity.calculateCosineSimilarity(title, m2.getTitle()),
                            CosineSimilarity.calculateCosineSimilarity(title, m1.getTitle())))
                    .map(MoviesDto::createMoviesDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in recommendMoviesByTitle: ", e);
            throw e;
        }
    }

    class CosineSimilarity {

        public static double calculateCosineSimilarity(String text1, String text2) {
            Map<Character, Integer> vec1 = textToVector(text1);
            Map<Character, Integer> vec2 = textToVector(text2);

            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;
            for (Character key : vec1.keySet()) {
                int val1 = vec1.getOrDefault(key, 0);
                int val2 = vec2.getOrDefault(key, 0);
                dotProduct += val1 * val2;
                normA += Math.pow(val1, 2);
                normB += Math.pow(val2, 2);
            }
            for (Character key : vec2.keySet()) {
                if (!vec1.containsKey(key)) {
                    int val2 = vec2.getOrDefault(key, 0);
                    normB += Math.pow(val2, 2);
                }
            }
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }

        private static Map<Character, Integer> textToVector(String text) {
            Map<Character, Integer> vector = new HashMap<>();
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c)) {
                    c = Character.toLowerCase(c);
                    vector.put(c, vector.getOrDefault(c, 0) + 1);
                }
            }
            return vector;
        }
    }
}

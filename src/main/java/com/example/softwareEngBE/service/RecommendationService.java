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

            List<Movies> allMovies = moviesRepository.findAll();
            logger.info("Found {} movies in total", allMovies.size());

            return allMovies.stream()
                    .filter(m -> !m.getTitle().equals(title))
                    .sorted((m1, m2) -> Double.compare(
                            calculateGenreCosineSimilarity(genre, m2.getGenres()),
                            calculateGenreCosineSimilarity(genre, m1.getGenres())))
                    .map(MoviesDto::createMoviesDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in recommendMoviesByTitle: ", e);
            throw e;
        }
    }

    private double calculateGenreCosineSimilarity(String genre1, String genre2) {
        Map<String, Integer> vec1 = genreToVector(genre1);
        Map<String, Integer> vec2 = genreToVector(genre2);

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (String key : vec1.keySet()) {
            int val1 = vec1.getOrDefault(key, 0);
            int val2 = vec2.getOrDefault(key, 0);
            dotProduct += val1 * val2;
            normA += Math.pow(val1, 2);
            normB += Math.pow(val2, 2);
        }
        for (String key : vec2.keySet()) {
            if (!vec1.containsKey(key)) {
                int val2 = vec2.getOrDefault(key, 0);
                normB += Math.pow(val2, 2);
            }
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private Map<String, Integer> genreToVector(String genres) {
        Map<String, Integer> vector = new HashMap<>();
        String[] genreArray = genres.split(",");
        for (String genre : genreArray) {
            genre = genre.trim().toLowerCase();
            vector.put(genre, vector.getOrDefault(genre, 0) + 1);
        }
        return vector;
    }
}
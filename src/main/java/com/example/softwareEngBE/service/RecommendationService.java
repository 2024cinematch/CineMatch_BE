package com.example.softwareEngBE.service;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.entity.Comment;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.repository.CommentRepository;
import com.example.softwareEngBE.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MoviesRepository moviesRepository;

    public List<MoviesDto> recommendMoviesBasedOnCosineSimilarity(long userId) {
        List<Long> userIds = commentRepository.findAllUserIds();
        List<Integer> movieIds = commentRepository.findAllMovieIds();

        double[][] ratingMatrix = new double[userIds.size()][movieIds.size()];
        for (int i = 0; i < userIds.size(); i++) {
            for (int j = 0; j < movieIds.size(); j++) {
                Comment comment = commentRepository.findByUserIdAndMovieId(userIds.get(i), movieIds.get(j));
                ratingMatrix[i][j] = (comment != null) ? comment.getRating() : 0;
            }
        }

        double[][] similarityMatrix = new double[movieIds.size()][movieIds.size()];
        for (int i = 0; i < movieIds.size(); i++) {
            for (int j = 0; j < movieIds.size(); j++) {
                similarityMatrix[i][j] = cosineSimilarity(ratingMatrix, i, j);
            }
        }

        List<Comment> userComments = commentRepository.findByUserId(userId);
        Set<Integer> ratedMovieIds = userComments.stream().map(Comment::getMovies).map(Movies::getMovie_Id).collect(Collectors.toSet());

        Map<Integer, Double> recommendationScores = new HashMap<>();
        for (Integer ratedMovieId : ratedMovieIds) {
            int movieIndex = movieIds.indexOf(ratedMovieId);
            for (int i = 0; i < movieIds.size(); i++) {
                if (!ratedMovieIds.contains(movieIds.get(i))) {
                    recommendationScores.put(movieIds.get(i), recommendationScores.getOrDefault(movieIds.get(i), 0.0) + similarityMatrix[movieIndex][i]);
                }
            }
        }

        List<Map.Entry<Integer, Double>> sortedRecommendations = recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<MoviesDto> recommendedMovies = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : sortedRecommendations) {
            Movies movie = moviesRepository.findById(entry.getKey()).orElse(null);
            if (movie != null) {
                recommendedMovies.add(MoviesDto.createMoviesDto(movie));
            }
        }

        return recommendedMovies;
    }
    public List<MoviesDto> recommendMoviesBasedOnMovieTitleAndRating(String title, float rating) {
        Movies targetMovie = moviesRepository.findByTitle(title).stream().findFirst().orElse(null);
        if (targetMovie == null) {
            return Collections.emptyList();
        }
        int targetMovieId = targetMovie.getMovie_Id();

        List<Long> userIds = commentRepository.findAllUserIds();
        List<Integer> movieIds = commentRepository.findAllMovieIds();

        double[][] ratingMatrix = new double[userIds.size()][movieIds.size()];
        for (int i = 0; i < userIds.size(); i++) {
            for (int j = 0; j < movieIds.size(); j++) {
                Comment comment = commentRepository.findByUserIdAndMovieId(userIds.get(i), movieIds.get(j));
                ratingMatrix[i][j] = (comment != null) ? comment.getRating() : 0;
            }
        }

        double[][] similarityMatrix = new double[movieIds.size()][movieIds.size()];
        for (int i = 0; i < movieIds.size(); i++) {
            for (int j = 0; j < movieIds.size(); j++) {
                similarityMatrix[i][j] = cosineSimilarity(ratingMatrix, i, j);
            }
        }

        int targetMovieIndex = movieIds.indexOf(targetMovieId);

        Map<Integer, Double> recommendationScores = new HashMap<>();
        for (int i = 0; i < movieIds.size(); i++) {
            if (i != targetMovieIndex) {
                recommendationScores.put(movieIds.get(i), similarityMatrix[targetMovieIndex][i]);
            }
        }

        List<Map.Entry<Integer, Double>> sortedRecommendations = recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<MoviesDto> recommendedMovies = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : sortedRecommendations) {
            Movies movie = moviesRepository.findById(entry.getKey()).orElse(null);
            if (movie != null) {
                recommendedMovies.add(MoviesDto.createMoviesDto(movie));
            }
        }

        return recommendedMovies;
    }


    private double cosineSimilarity(double[][] matrix, int index1, int index2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < matrix.length; i++) {
            dotProduct += matrix[i][index1] * matrix[i][index2];
            norm1 += Math.pow(matrix[i][index1], 2);
            norm2 += Math.pow(matrix[i][index2], 2);
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
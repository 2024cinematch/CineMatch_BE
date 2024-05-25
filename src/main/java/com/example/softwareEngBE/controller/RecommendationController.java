package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/recommend/user/{userId}")
    public ResponseEntity<List<MoviesDto>> recommendMoviesBasedOnUser(@PathVariable long userId) {
        List<MoviesDto> recommendedMovies = recommendationService.recommendMoviesBasedOnCosineSimilarity(userId);
        if (recommendedMovies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recommendedMovies);
    }

    @GetMapping("/recommend/movie")
    public ResponseEntity<List<MoviesDto>> recommendMoviesBasedOnMovieTitleAndRating(@RequestParam String title, @RequestParam float rating) {
        List<MoviesDto> recommendedMovies = recommendationService.recommendMoviesBasedOnMovieTitleAndRating(title, rating);
        if (recommendedMovies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recommendedMovies);
    }
}
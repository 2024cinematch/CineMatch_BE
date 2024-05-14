package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.repository.MoviesRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MoviesRecommendationController {
    @Autowired
    private MoviesRepository moviesRepository;

    @GetMapping("/recommend/{movieId}")
    @Operation(summary = "시청한 영화를 기반으로 유사한 장르의 영화 추천",
            description = "특정 영화를 시청한 후 해당 영화의 장르와 유사한 장르를 가진 다른 영화를 추천합니다.")
    public ResponseEntity<List<MoviesDto>> recommendMoviesBasedOnWatched(@PathVariable Integer movieId) {
        Movies watchedMovie = moviesRepository.findById(movieId).orElse(null);
        if (watchedMovie == null) {
            return ResponseEntity.notFound().build();
        }

        String[] genres = watchedMovie.getGenres().split("\\|");

        List<MoviesDto> recommendedMovies = moviesRepository.findAll()
                .stream()
                .filter(movie -> movie.getMovie_Id() != movieId) // 동일 영화 제외
                .filter(movie -> {
                    String[] movieGenres = movie.getGenres().split("\\|");
                    return Arrays.stream(genres).anyMatch(gen -> Arrays.asList(movieGenres).contains(gen));
                })
                .map(MoviesDto::createMoviesDto)
                .collect(Collectors.toList());

        if (recommendedMovies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(recommendedMovies);
    }
}
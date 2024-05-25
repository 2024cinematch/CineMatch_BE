package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.service.MoviesSearchService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreMoviesController {

    @Autowired
    private MoviesSearchService moviesSearchService;

    // 선택한 장르에 해당하는 모든 영화를 조회
    @GetMapping("/genre/{genre}/movies")
    @Operation(summary = "장르별 영화 조회",
            description = "지정된 장르의 모든 영화를 조회합니다.")
    public ResponseEntity<List<MoviesDto>> getMoviesByGenre(@PathVariable String genre) {
        List<MoviesDto> movies = moviesSearchService.getMoviesByGenre(genre);

        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(movies);
    }

//    // 선택한 장르에 해당하는 모든 영화를 평점순으로 조회
//    @GetMapping("/genre/{genre}/movies")
//    @Operation(summary = "장르별 영화 평점순 조회",
//            description = "지정된 장르의 모든 영화를 평점이 높은 순서로 조회합니다.")
//    public ResponseEntity<List<MoviesDto>> getMoviesByGenreOrderByRating(@PathVariable String genre) {
//        List<MoviesDto> movies = moviesSearchService.getMoviesByGenreOrderByRating(genre);
//
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(movies);
//    }
}

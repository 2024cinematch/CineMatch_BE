package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.service.MoviesSearchService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoviesSearchController {
    @Autowired
    MoviesSearchService moviesSearchService;

    // 모든 영화 조회 기능
    @GetMapping("/all")
    @Operation(summary = "모든 영화 조회",
            description = "등록된 모든 영화의 목록을 조회합니다.")
    public ResponseEntity<List<MoviesDto>> getAllMovies() {
        List<MoviesDto> dtos = moviesSearchService.getAllMovies();
        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtos);
    }

    // 영화 제목으로 유사한 영화 검색 기능
    @GetMapping("/search/{title}")
    @Operation(summary = "영화 제목으로 유사한 영화 검색",
            description = "특정 영화 제목과 유사한 영화 목록을 검색합니다.")
    public ResponseEntity<List<MoviesDto>> searchMovies(@PathVariable(required = false) String title) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<MoviesDto> movies = moviesSearchService.searchMoviesByTitle(title);
        return ResponseEntity.ok(movies);
    }
}

package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.service.MoviesSearchService;
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

    @GetMapping("/all")
    public ResponseEntity<List<MoviesDto>> getAllMovies() {
        List<MoviesDto> dtos = moviesSearchService.getAllMovies();
        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<MoviesDto>> getMovieDetails(@PathVariable String title)
    {
        List<MoviesDto> moviesDto= moviesSearchService.findSimilarMoviesByTitle(title);
        if (moviesDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(moviesDto);
    }
}

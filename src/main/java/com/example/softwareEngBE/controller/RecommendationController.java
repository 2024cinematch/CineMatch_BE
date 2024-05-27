package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/title/{title}")
    public List<MoviesDto> recommendMoviesByTitle(@PathVariable String title) {
        return recommendationService.recommendMoviesByTitle(title);
    }

}

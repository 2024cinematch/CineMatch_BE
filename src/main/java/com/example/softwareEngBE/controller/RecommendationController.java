package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    // 추천 서비스 객체를 주입받는 생성자
    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "제목으로 영화 추천",
            description = "제공된 제목을 기반으로 추천 영화를 목록으로 가져옵니다.")
    @GetMapping("/title/{title}")
    public List<MoviesDto> recommendMoviesByTitle(@PathVariable String title) {
        return recommendationService.recommendMoviesByTitle(title);
    }
}

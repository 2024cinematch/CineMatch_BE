package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.CommentDto;
import com.example.softwareEngBE.dto.MoviesDto;


import com.example.softwareEngBE.service.CommentService;
import com.example.softwareEngBE.service.MoviesSearchService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ToString
@Slf4j
@RestController
public class CommentApiController {
    @Autowired CommentService commentService;
    @Autowired MoviesSearchService moviesSearchService;

    //댓글 목록 조회
    @GetMapping("/search/{title}/comments")
    @Operation(summary = "영화 제목으로 댓글 조회",
            description = "특정 영화의 댓글 목록을 조회합니다.")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable String title) {
        List<CommentDto> dtos=commentService.comments(title);

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    //댓글 생성
    @PostMapping("/search/{title}/comments")
    @Operation(summary = "영화에 댓글 추가",
            description = "특정 영화에 새로운 댓글을 추가합니다.")
    public ResponseEntity<CommentDto> addComment(@PathVariable String title,@RequestBody CommentDto dto) {
        log.info("received Dto{}",dto.toString());
        int id= moviesSearchService.findByTitleToId(title);
        CommentDto commentDto=commentService.create(id,dto);

        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

    @GetMapping("/api/movie/{title}")
    @Operation(summary = "제목으로 영화 정보 가져오기",
            description = "제공된 제목을 기반으로 영화 정보를 가져옵니다.")
    public ResponseEntity<MoviesDto> getMovieByTitle(@PathVariable String title) {
        MoviesDto movieDetail = moviesSearchService.getMovieByTitle(title);
        if (movieDetail != null) {
            return ResponseEntity.ok(movieDetail);
        } else {
            // 해당 제목의 영화를 찾지 못한 경우
            return ResponseEntity.notFound().build();
        }
    }


}

package com.example.softwareEngBE.controller;

import com.example.softwareEngBE.dto.CommentDto;
import com.example.softwareEngBE.service.CommentService;
import com.example.softwareEngBE.service.MoviesSearchService;

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
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable String title) {
        List<CommentDto> dtos=commentService.comments(title);

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    //댓글 생성
    @PostMapping("/search/{title}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable String title,@RequestBody CommentDto dto) {
        log.info("received Dto{}",dto.toString());
        int id= moviesSearchService.findByTitletoId(title);
        CommentDto commentDto=commentService.create(id,dto);

        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }
}


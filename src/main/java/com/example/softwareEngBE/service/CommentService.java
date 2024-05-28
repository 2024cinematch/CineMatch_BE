package com.example.softwareEngBE.service;

import com.example.softwareEngBE.dto.CommentDto;
import com.example.softwareEngBE.entity.Comment;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.repository.CommentRepository;
import com.example.softwareEngBE.repository.MoviesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MoviesRepository moviesRepository;

    @Autowired MoviesSearchService moviesSearchService;

    public List<CommentDto> comments(String title) {
        List<Comment> comments = commentRepository.findByMovieTitleIgnoreCase(title);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment c : comments) {
            CommentDto dto = CommentDto.createCommentDto(c);
            commentDtos.add(dto);
        }
        return commentDtos;
    }

//생성
    public CommentDto create(int id, CommentDto dto) {
    Movies movies = moviesRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("No exist movie"));
    //댓글 엔티티 생성
    Comment comment=Comment.createComment(dto,movies);
    Comment created=commentRepository.save(comment);
    return CommentDto.createCommentDto(created);
    }
}

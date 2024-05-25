package com.example.softwareEngBE.dto;

import com.example.softwareEngBE.entity.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Slf4j
@Data
@Setter
public class CommentDto {
    @JsonProperty("id")
    private long user_Id;

    @JsonProperty("movie_Id")
    private int movie_Id;

    @JsonProperty("rating")
    private float rating;

    @JsonProperty("comment")
    private String comment;


    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getUser_id(),
                comment.getMovies().getMovie_Id(),
                comment.getRating(),
                comment.getComment()
        );
    }
}

package com.example.softwareEngBE.entity;

import com.example.softwareEngBE.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long user_id;

    @ManyToOne // 해당 댓글 엔티티 여러개가 하나의 Movie에 연관
    @JoinColumn(name="movie_id")//외래 참조
    private Movies movies;

    @Column
    private float rating;
    @Column
    private String comment;

    public static Comment createComment(CommentDto dto,Movies movies){
        return new Comment(
                dto.getUser_Id(),
                movies,
                dto.getRating(),
                dto.getComment()
        );
    }

}


package com.example.softwareEngBE.entity;

import com.example.softwareEngBE.dto.CommentDto;
import com.example.softwareEngBE.dto.MoviesDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Movies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movie_Id;
    @Column
    private String title;
    @Column
    private String genres;
//    @JoinColumn(name = "rating")
//    private Float rating; // 영화 평점 필드 추가

    public static Movies createMovies(MoviesDto dto, CommentDto commentDto){
        return new Movies(
                dto.getMovie_Id(),
                dto.getTitle(),
                dto.getGenres()
//                commentDto.getRating()
        );
    }
}

package com.example.softwareEngBE.dto;

import com.example.softwareEngBE.entity.Movies;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class MoviesDto {
    private int movie_Id;
    private String title;
    private String genres;
//    private float rating;

    public static MoviesDto createMoviesDto(Movies movies)
    {
        return new MoviesDto(
                movies.getMovie_Id(),
                movies.getTitle(),
                movies.getGenres()
//                movies.getRating());
        );
    }
}

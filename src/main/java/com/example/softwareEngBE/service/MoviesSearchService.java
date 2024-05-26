package com.example.softwareEngBE.service;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.logic.CosineSimilarity;
import com.example.softwareEngBE.repository.MoviesRepository;

import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;



@Service
public class MoviesSearchService {
    @Autowired
    MoviesRepository moviesRepository;
    @Autowired
    CosineSimilarity cosineSimilarity;

    public List<MoviesDto> getAllMovies() {
        List<Movies> moviesEntityList = moviesRepository.findAll();
        List<MoviesDto> moviesDtoList = new ArrayList<>();
        for (Movies movieEntity : moviesEntityList) {
            moviesDtoList.add(MoviesDto.createMoviesDto(movieEntity));
        }
        return moviesDtoList;
    }

//    // 선택한 장르에 해당하는 모든 영화를 평점 순으로 가져오는 메서드
//    public List<MoviesDto> getMoviesByGenreOrderByRating(String genre) {
//        List<Movies> movies = moviesRepository.findByGenresContainingIgnoreCase(genre);
//
//        // 영화를 평점 순으로 정렬하여 MoviesDto 리스트로 매핑
//        List<MoviesDto> moviesDtos = movies.stream()
//                .sorted((m1, m2) -> Float.compare(m2.getRating(), m1.getRating())) // 평점 역순 정렬
//                .map(MoviesDto::createMoviesDto)
//                .collect(Collectors.toList());
//
//        return moviesDtos;
//    }

    // 선택한 장르에 해당하는 모든 영화를 가져오는 메서드
    public List<MoviesDto> getMoviesByGenre(String genre) {
        List<Movies> movies = moviesRepository.findByGenresContainingIgnoreCase(genre);

        // Movies 엔티티 리스트를 MoviesDto 리스트로 변환하여 반환
        List<MoviesDto> moviesDtos = movies.stream()
                .map(MoviesDto::createMoviesDto)
                .collect(Collectors.toList());

        return moviesDtos;
    }

//    //제목으로 찾기
//    public List<MoviesDto> findByTitle(String title) {
//        List<Movies> moviesEntityList=moviesRepository.findByTitle(title);
//        List<MoviesDto> moviesDtoList=new ArrayList<>();
//        for (Movies movieEntity : moviesEntityList) {
//            moviesDtoList.add(MoviesDto.createMoviesDto(movieEntity));
//        }
//        return moviesDtoList;
//    }


    //Cosine 유사도로 검색
    public List<MoviesDto> searchMoviesByTitle(String title) {
        List<Movies> movies = moviesRepository.searchByTitleIgnoringCase(title);


        List<MoviesDto> sortedMovies = movies.stream()
                .sorted(Comparator.comparingInt(movie -> CosineSimilarity.computeLevenshteinDistance(
                        movie.getTitle().replace(" ", "").toLowerCase(), title.replace(" ", "").toLowerCase())))
                .map(MoviesDto::createMoviesDto)
                .collect(Collectors.toList());

        return sortedMovies;
    }


    //title로 id찾기 comment서비스에 필요함
    public int findByTitletoId(String title) {
        Movies movie =moviesRepository.findByTitletoId(title);
        MoviesDto moviesDto=MoviesDto.createMoviesDto(movie);
        return moviesDto.getMovie_Id();
    }
    
    // 영화 제목으로 영화 정보(제목과 장르, 평점)를 가져오는 메서드
    public MoviesDto getMovieByTitle(String title) {
        Movies movie = moviesRepository.findDetailByTitle(title);
        if (movie != null) {
            return MoviesDto.createMoviesDto(movie);
        } else {
            // 해당 제목의 영화를 찾지 못한 경우
            return null; // 예외 처리에 따라 적절한 값을 반환합니다.
        }
    }


    
}

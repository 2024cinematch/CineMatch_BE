package com.example.softwareEngBE.service;

import com.example.softwareEngBE.dto.MoviesDto;
import com.example.softwareEngBE.entity.Movies;
import com.example.softwareEngBE.logic.CosineSimilarity;
import com.example.softwareEngBE.repository.MoviesRepository;

import org.apache.commons.math3.linear.RealVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.softwareEngBE.logic.CosineSimilarity.cosineSimilarity;


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


//    //제목으로 찾기
//    public List<MoviesDto> findByTitle(String title) {
//        List<Movies> moviesEntityList=moviesRepository.findByTitle(title);
//        List<MoviesDto> moviesDtoList=new ArrayList<>();
//        for (Movies movieEntity : moviesEntityList) {
//            moviesDtoList.add(MoviesDto.createMoviesDto(movieEntity));
//        }
//        return moviesDtoList;
//    }

    // 코사인 유사도 기반의 유사 제목 검색
    public List<MoviesDto> findSimilarMoviesByTitle(String inputTitle) {
        // 데이터베이스에서 제목의 일부를 포함하는 영화들을 먼저 검색
        List<Movies> filteredMovies = moviesRepository.findByTitleContaining(inputTitle);
        List<MoviesDto> similarMovies = new ArrayList<>();
        CosineSimilarity.initializeGlobalWordSet(filteredMovies.stream().map(Movies::getTitle).collect(Collectors.toList()));

        // 입력 제목과 각 영화 제목 간의 유사도를 계산하여 유사한 영화를 선택
        for (Movies movie : filteredMovies) {
            double similarity = cosineSimilarity(inputTitle, movie.getTitle());
            // 유사도 임계값 설정
            if (similarity > 0.1) {
                similarMovies.add(MoviesDto.createMoviesDto(movie));
            }
        }
        return similarMovies;
    }




    //title로 id찾기 comment서비스에 필요함
    public int findByTitletoId(String title) {
        Movies movie =moviesRepository.findByTitletoId(title);
        MoviesDto moviesDto=MoviesDto.createMoviesDto(movie);
        return moviesDto.getMovie_Id();
    }
}

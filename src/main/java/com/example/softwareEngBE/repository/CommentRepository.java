package com.example.softwareEngBE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.softwareEngBE.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    //특정 게시글의 모든 댓글 조회
    @Query(value = "Select * " +
            "from comment " +
            "where movie_Id = :id",
            nativeQuery = true)
    List<Comment> findByMovieId(int id);
    @Query(value = "SELECT DISTINCT user_id FROM comment", nativeQuery = true)
    List<Long> findAllUserIds();

    @Query(value = "SELECT DISTINCT movie_id FROM comment", nativeQuery = true)
    List<Integer> findAllMovieIds();

    @Query(value = "SELECT * FROM comment WHERE user_id = :userId AND movie_id = :movieId", nativeQuery = true)
    Comment findByUserIdAndMovieId(long userId, int movieId);

    @Query(value = "SELECT * FROM comment WHERE user_id = :userId", nativeQuery = true)
    List<Comment> findByUserId(long userId);
}
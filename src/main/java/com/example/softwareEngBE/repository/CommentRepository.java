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
}

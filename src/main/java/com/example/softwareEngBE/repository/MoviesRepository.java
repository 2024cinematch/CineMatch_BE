package com.example.softwareEngBE.repository;

import com.example.softwareEngBE.entity.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends JpaRepository<Movies,Integer> {
    Movies findByTitle(String title);
    @Query(value = "Select * " +
            "from movies " +
            "where title = :title",
            nativeQuery = true)
    Movies findByTitleToId(String title);
    List<Movies> findByTitleContaining(String title);
    List<Movies> findByGenresContainingIgnoreCase(String genre);
    Movies findDetailByTitleIgnoreCase(String title);
    @Query("SELECT m FROM Movies m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movies> searchByTitleIgnoringCase(String title);
}

package com.project.huytnfx12164.repository;


import com.project.huytnfx12164.model.Account;
import com.project.huytnfx12164.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public interface NewsRepository extends JpaRepository<News, Integer>{
    @Query(value = "select * from NEWS where TITLE LIKE %:keyword% or CONTENT like %:keyword% or DATE like %:keyword% or COUNTVIEW like %:keyword% ", nativeQuery = true)
    List<News> findByKeyword(@Param("keyword") String keyword);
    @Query(value = "SELECT  * FROM NEWS where STATUS = 1", nativeQuery = true)
    List<News> getAll();

    @Query(value = "select * from NEWS where TITLE = :title ", nativeQuery = true)
    News findByTitle(@Param("title") String title);

    @Query(value = "select  COUNT( distinct ID) from NEWS ", nativeQuery = true)
    int total();

    @Query(value = "SELECT TOP 5 * FROM NEWS ORDER BY COUNTVIEW DESC", nativeQuery = true)
    List<News> topList();

    @Query(value = "SELECT  * FROM NEWS ORDER BY DATE DESC",
            countQuery = "SELECT COUNT(distinct ID) FROM NEWS ",nativeQuery = true)
    Page<News> page(Pageable pageable);
    @Query(value = "SELECT  * FROM NEWS where STATUS = 1 ORDER BY DATE DESC",
            countQuery = "SELECT COUNT(distinct ID) FROM NEWS ",nativeQuery = true)
    Page<News> pageBlogs(Pageable pageable);


    @Query(value = "select * from NEWS where TITLE LIKE %:keyword% or CONTENT like %:keyword% or DATE like %:keyword% or COUNTVIEW like %:keyword% ORDER BY DATE DESC",
            countQuery = "SELECT COUNT(distinct ID) FROM NEWS where TITLE LIKE %:keyword% or CONTENT like %:keyword% or DATE like %:keyword% or COUNTVIEW like %:keyword% ",nativeQuery = true)
    Page<News> pageSearch(@Param("keyword") String keyword,Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM NEWS where ID in (?1) ", nativeQuery = true)
    void delMultiple(@Param("ID") List<Integer> configID);
}

package com.blog.application.repository;

import com.blog.application.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByTags(String tag);
    List<Blog> findByUserId(Integer userId);
    Page<Blog> findAll(Pageable pageable);



}

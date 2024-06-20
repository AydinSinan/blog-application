package com.blog.application.service;

import com.blog.application.model.Blog;
import com.blog.application.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog getBlogById(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        return optionalBlog.orElse(null);
    }

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, Blog updatedBlogPost) {
        return blogRepository.findById(id).map(blogPost -> {
            blogPost.setTitle(updatedBlogPost.getTitle());
            blogPost.setText(updatedBlogPost.getText());
            return blogRepository.save(blogPost);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

// transactional
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public Blog addTag(Long id, String tag) {
        return blogRepository.findById(id).map(blogPost -> {
            blogPost.getTags().add(tag);
            return blogRepository.save(blogPost);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Blog removeTag(Long id, String tag) {
        return blogRepository.findById(id).map(blogPost -> {
            blogPost.getTags().remove(tag);
            return blogRepository.save(blogPost);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Blog> getBlogsByTag(String tag) {
        return blogRepository.findByTags(tag);
    }

}

package com.blog.application.service;

import com.blog.application.model.Blog;
import com.blog.application.model.User;
import com.blog.application.repository.BlogRepository;
import com.blog.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;


    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog createBlog(Blog blog, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid user email address" + email));
        blog.setUser(user);
        return blogRepository.save(blog);
    }

    public Blog getBlogById(Long id) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        return optionalBlog.orElse(null);
    }

    public Blog updateBlog(Long id, Blog updatedBlogPost) {
        return blogRepository.findById(id).map(blogPost -> {
            blogPost.setTitle(updatedBlogPost.getTitle());
            blogPost.setText(updatedBlogPost.getText());
            return blogRepository.save(blogPost);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

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

    public void addS3ObjectUrlToBlog(Long blogId, String s3ObjectUrl) {
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blog.setS3ObjectUrl(s3ObjectUrl);
            blogRepository.save(blog);
        } else {
            throw new IllegalArgumentException("Blog not found with id: " + blogId);
        }
    }
}

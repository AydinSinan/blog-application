package com.blog.application.controller;

import com.blog.application.dao.request.CreateBlogRequest;
import com.blog.application.model.Blog;
import com.blog.application.model.User;
import com.blog.application.service.BlogService;
import com.blog.application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(BlogController.class);



    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.getBlogById(id);
        if (blog != null) {
            // Kullanıcı bilgisini set etmeden geri döndürüyoruz
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

@PostMapping
public ResponseEntity<Blog> createBlog(@AuthenticationPrincipal @RequestBody CreateBlogRequest createBlogRequest) {
    String userEmail = createBlogRequest.getEmail();

    User user = userService.findByEmail(userEmail);
    //UserDetails user = userService.loadUserByUsername(userEmail);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

   // log.info("Creating blog for user with ID: {}", user.getId());
    log.info("Creating blog : {}", user.getUsername());
    Blog blog = new Blog();
    blog.setTitle(createBlogRequest.getTitle());
    blog.setText(createBlogRequest.getText());
    //blog.setTags(createBlogRequest.getTags());
    blog.setUser(user);

    // Blogu oluştur ve kaydet
    Blog createdBlog = blogService.createBlog(blog, user.getEmail());
    log.info("Created blog: {}", createdBlog);

    return ResponseEntity.ok(createdBlog);
}

/*
    @GetMapping
    public ResponseEntity<List<Blog>> getUserBlogs(@AuthenticationPrincipal UserDetails userDetails) {
        List<Blog> blogs = blogService.getUserBlogs(((User) userDetails).getId());
        return ResponseEntity.ok(blogs);
    }
*/
    @GetMapping
    public ResponseEntity<List<Blog>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id,
                                           @RequestBody Blog blog) {
        Blog updatedBlog = blogService.updateBlog(id, blog);
        if (updatedBlog != null) {
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/add-tag")
    public ResponseEntity<Blog> addTagToBlog(@PathVariable Long id,
                                             @RequestParam String tag) {
        Blog updatedBlog = blogService.addTag(id, tag);
        if (updatedBlog != null) {
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/remove-tag")
    public ResponseEntity<Blog> removeTagFromBlog(@PathVariable Long id,
                                                  @RequestParam String tag) {
        Blog updatedBlog = blogService.removeTag(id, tag);
        if (updatedBlog != null) {
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tags/{tag}")
    public List<Blog> getPostsByTag(@PathVariable String tag) {
        return blogService.getBlogsByTag(tag);
    }

}

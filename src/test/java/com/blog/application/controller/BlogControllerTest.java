package com.blog.application.controller;

import com.blog.application.model.Blog;
import com.blog.application.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @InjectMocks
    private BlogController blogController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBlogs() {
        Blog blog1 = new Blog();
        Blog blog2 = new Blog();
        when(blogService.getAllBlogs())
                .thenReturn(Arrays.asList(blog1, blog2));

        ResponseEntity<List<Blog>> response = blogController.getAllBlogs();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(blogService, times(1)).getAllBlogs();
    }

    @Test
    void testGetBlogById() {
        Blog blog = new Blog();
        when(blogService.getBlogById(1L))
                .thenReturn(blog);

        ResponseEntity<Blog> response = blogController.getBlogById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(blogService, times(1)).getBlogById(1L);
    }

    @Test
    void testGetBlogByIdNotFound() {
        when(blogService.getBlogById(1L))
                .thenReturn(null);

        ResponseEntity<Blog> response = blogController.getBlogById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(blogService, times(1)).getBlogById(1L);
    }

    @Test
    void testCreateBlog() {
        Blog blog = new Blog();
        when(blogService.createBlog(blog))
                .thenReturn(blog);

        ResponseEntity<Blog> response = blogController.createBlog(blog);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(blogService, times(1)).createBlog(blog);
    }

    @Test
    void testUpdateBlog() {
        Blog blog = new Blog();
        when(blogService.updateBlog(1L, blog))
                .thenReturn(blog);

        ResponseEntity<Blog> response = blogController.updateBlog(1L, blog);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(blogService, times(1)).updateBlog(1L, blog);
    }

    @Test
    void testUpdateBlogNotFound() {
        Blog blog = new Blog();
        when(blogService.updateBlog(1L, blog))
                .thenReturn(null);

        ResponseEntity<Blog> response = blogController.updateBlog(1L, blog);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(blogService, times(1)).updateBlog(1L, blog);
    }

    @Test
    void testDeleteBlog() {
        doNothing().when(blogService).deleteBlog(1L);

        ResponseEntity<Void> response = blogController.deleteBlog(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(blogService, times(1)).deleteBlog(1L);
    }

    @Test
    void testAddTagToBlog() {
        Blog blog = new Blog();
        when(blogService.addTag(1L, "new-tag"))
                .thenReturn(blog);

        ResponseEntity<Blog> response = blogController.addTagToBlog(1L, "new-tag");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(blogService, times(1)).addTag(1L, "new-tag");
    }

    @Test
    void testAddTagToBlogNotFound() {
        when(blogService.addTag(1L, "new-tag"))
                .thenReturn(null);

        ResponseEntity<Blog> response = blogController.addTagToBlog(1L, "new-tag");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(blogService, times(1)).addTag(1L, "new-tag");
    }

    @Test
    void testRemoveTagFromBlog() {
        Blog blog = new Blog();
        when(blogService.removeTag(1L, "old-tag"))
                .thenReturn(blog);

        ResponseEntity<Blog> response = blogController.removeTagFromBlog(1L, "old-tag");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(blogService, times(1)).removeTag(1L, "old-tag");
    }

    @Test
    void testRemoveTagFromBlogNotFound() {
        when(blogService.removeTag(1L, "old-tag"))
                .thenReturn(null);

        ResponseEntity<Blog> response = blogController.removeTagFromBlog(1L, "old-tag");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(blogService, times(1)).removeTag(1L, "old-tag");
    }

    @Test
    void testGetPostsByTag() {
        Blog blog1 = new Blog();
        Blog blog2 = new Blog();
        when(blogService.getBlogsByTag("tag"))
                .thenReturn(Arrays.asList(blog1, blog2));

        List<Blog> blogs = blogController.getPostsByTag("tag");
        assertEquals(2, blogs.size());
        verify(blogService, times(1)).getBlogsByTag("tag");
    }

}

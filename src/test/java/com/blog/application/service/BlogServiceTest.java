package com.blog.application.service;

import com.blog.application.model.Blog;
import com.blog.application.model.User;
import com.blog.application.repository.BlogRepository;
import com.blog.application.repository.UserRepository;
import com.blog.application.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogService blogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllBlogs() {
        // Given
        List<Blog> mockBlogs = new ArrayList<>();
        mockBlogs.add(new Blog(1L, "Title 1", "Text 1", new HashSet<>()));
        mockBlogs.add(new Blog(2L, "Title 2", "Text 2", new HashSet<>()));
        when(blogRepository.findAll()).thenReturn(mockBlogs);

        // When
        List<Blog> result = blogService.getAllBlogs();

        // Then
        assertEquals(2, result.size());
        verify(blogRepository, times(1)).findAll();
    }

    @Test
    void testGetBlogById() {
        // Given
        Long blogId = 1L;
        Blog mockBlog = new Blog(blogId, "Title", "Text", new HashSet<>());
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));

        // When
        Blog result = blogService.getBlogById(blogId);

        // Then
        assertNotNull(result);
        assertEquals(blogId, result.getId());
        verify(blogRepository, times(1)).findById(blogId);
    }

    @Test
    void testCreateBlog() {
        // Given
        Blog newBlog = new Blog(null, "New Title", "New Text", new HashSet<>());
        User user = new User();
        user.setId(1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(blogRepository.save(newBlog)).thenReturn(newBlog);

        // When
        Blog result = blogService.createBlog(newBlog, user.getEmail());

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(user.getId());
        verify(blogRepository, times(1)).save(newBlog);
    }


    @Test
    void testUpdateBlog() {
        // Given
        Long blogId = 1L;
        Blog existingBlog = new Blog(blogId, "Existing Title", "Existing Text", new HashSet<>());
        Blog updatedBlog = new Blog(blogId, "Updated Title", "Updated Text", new HashSet<>());
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(existingBlog));
        when(blogRepository.save(existingBlog)).thenReturn(updatedBlog);

        // When
        Blog result = blogService.updateBlog(blogId, updatedBlog);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Text", result.getText());
        verify(blogRepository, times(1)).findById(blogId);
        verify(blogRepository, times(1)).save(existingBlog);
    }

    @Test
    void testDeleteBlog() {
        // Given
        Long blogId = 1L;

        // When
        blogService.deleteBlog(blogId);

        // Then
        verify(blogRepository, times(1)).deleteById(blogId);
    }

    @Test
    void testAddTag() {
        // Given
        Long blogId = 1L;
        String tag = "Java";
        Blog mockBlog = new Blog(blogId, "Title", "Text", new HashSet<>());
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));
        when(blogRepository.save(mockBlog)).thenReturn(mockBlog);

        // When
        Blog result = blogService.addTag(blogId, tag);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTags().size());
        assertTrue(result.getTags().contains(tag));
        verify(blogRepository, times(1)).findById(blogId);
        verify(blogRepository, times(1)).save(mockBlog);
    }

    @Test
    void testRemoveTag() {
        // Given
        Long blogId = 1L;
        String tag = "Java";
        Set<String> tags = new HashSet<>();
        tags.add(tag);
        Blog mockBlog = new Blog(blogId, "Title", "Text", tags);
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));
        when(blogRepository.save(mockBlog)).thenReturn(mockBlog);

        // When
        Blog result = blogService.removeTag(blogId, tag);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTags().size());
        assertFalse(result.getTags().contains(tag));
        verify(blogRepository, times(1)).findById(blogId);
        verify(blogRepository, times(1)).save(mockBlog);
    }

    @Test
    void testGetBlogsByTag() {
        // Given
        String tag = "Java";
        List<Blog> mockBlogs = new ArrayList<>();
        mockBlogs.add(new Blog(1L, "Title 1", "Text 1", Set.of(tag)));
        mockBlogs.add(new Blog(2L, "Title 2", "Text 2", Set.of(tag)));
        when(blogRepository.findByTags(tag)).thenReturn(mockBlogs);

        // When
        List<Blog> result = blogService.getBlogsByTag(tag);

        // Then
        assertEquals(2, result.size());
        assertEquals(tag, result.get(0).getTags().iterator().next()); // Assuming only one tag for simplicity
        verify(blogRepository, times(1)).findByTags(tag);
    }
}

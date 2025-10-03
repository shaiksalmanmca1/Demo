//package com.cms.controller;
//
//import com.cms.config.TestConfig;
//import com.cms.dto.ArticleRequest;
//import com.cms.dto.ArticleResponse;
//import com.cms.service.ArticleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@Import(TestConfig.class)
//@ActiveProfiles("test")
//class ArticleControllerTest {
//
//    @Mock
//    private ArticleService articleService;
//
//    @InjectMocks
//    private ArticleController articleController;
//
//    private ArticleRequest articleRequest;
//    private ArticleResponse articleResponse;
//    private Page<ArticleResponse> articlePage;
//
//    @BeforeEach
//    void setUp() {
//        // Setup test data
//        articleRequest = new ArticleRequest();
//        articleRequest.setTitle("Test Article");
//        articleRequest.setContent("Test Content");
//
//        articleResponse = new ArticleResponse();
//        articleResponse.setId(1L);
//        articleResponse.setTitle("Test Article");
//        articleResponse.setContent("Test Content");
//        articleResponse.setAuthorUsername("testUser");
//        articleResponse.setCreatedAt(LocalDateTime.now());
//        articleResponse.setUpdatedAt(LocalDateTime.now());
//
//        List<ArticleResponse> articles = Arrays.asList(articleResponse);
//        articlePage = new PageImpl<>(articles);
//    }
//
//    @Test
//    void getAllArticles_ShouldReturnPageOfArticles() {
//        // Arrange
//        Pageable pageable = PageRequest.of(0, 10);
//        when(articleService.getAllArticles(any(Pageable.class))).thenReturn(articlePage);
//
//        // Act
//        ResponseEntity<Page<ArticleResponse>> response = articleController.getAllArticles(pageable);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(1, response.getBody().getTotalElements());
//        verify(articleService).getAllArticles(pageable);
//    }
//
//    @Test
//    void getArticle_ShouldReturnArticle() {
//        // Arrange
//        Long articleId = 1L;
//        when(articleService.getArticle(articleId)).thenReturn(articleResponse);
//
//        // Act
//        ResponseEntity<ArticleResponse> response = articleController.getArticle(articleId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(articleId, response.getBody().getId());
//        verify(articleService).getArticle(articleId);
//    }
//
//    @Test
//    void createArticle_ShouldReturnCreatedArticle() {
//        // Arrange
//        when(articleService.createArticle(any(ArticleRequest.class))).thenReturn(articleResponse);
//
//        // Act
//        ResponseEntity<ArticleResponse> response = articleController.createArticle(articleRequest);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(articleResponse.getTitle(), response.getBody().getTitle());
//        verify(articleService).createArticle(articleRequest);
//    }
//
//    @Test
//    void updateArticle_ShouldReturnUpdatedArticle() {
//        // Arrange
//        Long articleId = 1L;
//        when(articleService.updateArticle(eq(articleId), any(ArticleRequest.class)))
//                .thenReturn(articleResponse);
//
//        // Act
//        ResponseEntity<ArticleResponse> response = articleController.updateArticle(articleId, articleRequest);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(articleResponse.getTitle(), response.getBody().getTitle());
//        verify(articleService).updateArticle(articleId, articleRequest);
//    }
//
//    @Test
//    void deleteArticle_ShouldReturnNoContent() {
//        // Arrange
//        Long articleId = 1L;
//        doNothing().when(articleService).deleteArticle(articleId);
//
//        // Act
//        ResponseEntity<Void> response = articleController.deleteArticle(articleId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(204, response.getStatusCode().value());
//        verify(articleService).deleteArticle(articleId);
//    }
//
//    @Test
//    void getRecentlyViewedArticles_ShouldReturnPageOfArticles() {
//        // Arrange
//        Pageable pageable = PageRequest.of(0, 10);
//        when(articleService.getRecentlyViewedArticles(any(Pageable.class))).thenReturn(articlePage);
//
//        // Act
//        ResponseEntity<Page<ArticleResponse>> response = articleController.getRecentlyViewedArticles(pageable);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCode().value());
//        assertNotNull(response.getBody());
//        assertEquals(1, response.getBody().getTotalElements());
//        verify(articleService).getRecentlyViewedArticles(pageable);
//    }
//} 
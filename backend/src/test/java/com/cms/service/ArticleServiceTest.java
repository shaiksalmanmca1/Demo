//package com.cms.service;
//
//import com.cms.dto.ArticleRequest;
//import com.cms.dto.ArticleResponse;
//import com.cms.model.Article;
//import com.cms.model.RecentlyViewedArticle;
//import com.cms.model.User;
//import com.cms.repository.ArticleRepository;
//import com.cms.repository.RecentlyViewedArticleRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.data.domain.*;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ArticleServiceTest {
//
//    @InjectMocks
//    private ArticleService articleService;
//
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @Mock
//    private RecentlyViewedArticleRepository recentlyViewedRepository;
//
//    @Mock
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        articleService = new ArticleService(
//                articleRepository,
//                recentlyViewedRepository,
//                userService,
//                3  // maxRecentArticles
//        );
//    }
//
//    @Test
//    void testGetAllArticles() {
//        Article article = createSampleArticle();
//        Page<Article> page = new PageImpl<>(List.of(article));
//        when(articleRepository.findAll(any(Pageable.class))).thenReturn(page);
//
//        Page<ArticleResponse> result = articleService.getAllArticles(PageRequest.of(0, 10));
//
//        assertEquals(1, result.getTotalElements());
//        assertEquals("Sample Title", result.getContent().get(0).getTitle());
//    }
//
//    @Test
//    void testGetArticle_found() {
//        Article article = createSampleArticle();
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//        when(userService.getCurrentUser()).thenReturn(article.getAuthor());
//        when(recentlyViewedRepository.findByUserAndArticle(any(), any())).thenReturn(Optional.empty());
//        when(recentlyViewedRepository.countByUser(any())).thenReturn(0L);
//
//        ArticleResponse response = articleService.getArticle(1L);
//
//        assertEquals("Sample Title", response.getTitle());
//        verify(recentlyViewedRepository).save(any(RecentlyViewedArticle.class));
//    }
//
//    @Test
//    void testGetArticle_notFound() {
//        when(articleRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> articleService.getArticle(1L));
//    }
//
//    @Test
//    void testCreateArticle() {
//        ArticleRequest request = new ArticleRequest();
//        request.setTitle("New Article");
//        request.setContent("New Content");
//
//        User user = createSampleUser();
//        when(userService.getCurrentUser()).thenReturn(user);
//
//        Article savedArticle = createSampleArticle();
//        savedArticle.setTitle("New Article");
//        savedArticle.setContent("New Content");
//
//        when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);
//
//        ArticleResponse response = articleService.createArticle(request);
//
//        assertEquals("New Article", response.getTitle());
//        verify(articleRepository).save(any(Article.class));
//    }
//
//    @Test
//    void testUpdateArticle_success() {
//        Article article = createSampleArticle();
//        ArticleRequest request = new ArticleRequest();
//        request.setTitle("Updated");
//        request.setContent("Updated Content");
//
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//        when(userService.getCurrentUser()).thenReturn(article.getAuthor());
//        when(articleRepository.save(any(Article.class))).thenReturn(article);
//
//        ArticleResponse response = articleService.updateArticle(1L, request);
//
//        assertEquals("Updated", response.getTitle());
//    }
//
//    @Test
//    void testUpdateArticle_invalidOwner() {
//        Article article = createSampleArticle();
//        User anotherUser = new User();
//        anotherUser.setId(99L);
//
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//        when(userService.getCurrentUser()).thenReturn(anotherUser);
//
//        assertThrows(RuntimeException.class, () -> articleService.updateArticle(1L, new ArticleRequest()));
//    }
//
//    @Test
//    void testDeleteArticle_success() {
//        Article article = createSampleArticle();
//        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//        when(userService.getCurrentUser()).thenReturn(article.getAuthor());
//
//        articleService.deleteArticle(1L);
//
//        verify(articleRepository).delete(article);
//    }
//
//    @Test
//    void testGetRecentlyViewedArticles() {
//        Article article = createSampleArticle();
//        RecentlyViewedArticle view = new RecentlyViewedArticle();
//        view.setArticle(article);
//
//        Page<RecentlyViewedArticle> page = new PageImpl<>(List.of(view));
//
//        when(userService.getCurrentUser()).thenReturn(article.getAuthor());
//        when(recentlyViewedRepository.findRecentlyViewedByUser(any(), any()))
//                .thenReturn(page);
//
//        Page<ArticleResponse> result = articleService.getRecentlyViewedArticles(PageRequest.of(0, 5));
//
//        assertEquals(1, result.getTotalElements());
//        assertEquals("Sample Title", result.getContent().get(0).getTitle());
//    }
//
//    // Utility methods
//
//    private Article createSampleArticle() {
//        Article article = new Article();
//        article.setId(1L);
//        article.setTitle("Sample Title");
//        article.setContent("Sample Content");
//        article.setCreatedAt(LocalDateTime.now());
//        article.setUpdatedAt(LocalDateTime.now());
//        User user = createSampleUser();
//        article.setAuthor(user);
//        return article;
//    }
//
//    private User createSampleUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("john_doe");
//        return user;
//    }
//}

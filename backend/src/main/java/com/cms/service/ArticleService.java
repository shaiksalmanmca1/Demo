package com.cms.service;

import com.cms.dto.ArticleRequest;
import com.cms.dto.ArticleResponse;
import com.cms.model.Article;
import com.cms.model.RecentlyViewedArticle;
import com.cms.model.User;
import com.cms.repository.ArticleRepository;
import com.cms.repository.RecentlyViewedArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final RecentlyViewedArticleRepository recentlyViewedRepository;
    private final UserService userService;
    private final int maxRecentArticles;

    public ArticleService(
            @Lazy ArticleRepository articleRepository,
            @Lazy RecentlyViewedArticleRepository recentlyViewedRepository,
            @Lazy UserService userService,
            @Value("${app.recent-articles.max-size:10}") int maxRecentArticles) {
        this.articleRepository = articleRepository;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.userService = userService;
        this.maxRecentArticles = maxRecentArticles;
    }

    public Page<ArticleResponse> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public ArticleResponse getArticle(Long id) {
        Article article = findArticleById(id);
        trackRecentlyViewed(article);
        return convertToResponse(article);
    }

    public ArticleResponse createArticle(ArticleRequest request) {
        User currentUser = userService.getCurrentUser();
        
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setAuthor(currentUser);
        
        return convertToResponse(articleRepository.save(article));
    }

    public ArticleResponse updateArticle(Long id, ArticleRequest request) {
        Article article = findArticleById(id);
        validateOwnership(article);
        
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        
        return convertToResponse(articleRepository.save(article));
    }

    public void deleteArticle(Long id) {
        Article article = findArticleById(id);
        validateOwnership(article);
        articleRepository.delete(article);
    }

    public Page<ArticleResponse> getRecentlyViewedArticles(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return recentlyViewedRepository.findRecentlyViewedByUser(currentUser, pageable)
                .map(recentlyViewed -> convertToResponse(recentlyViewed.getArticle()));
    }

    private Article findArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
    }

    private void validateOwnership(Article article) {
        User currentUser = userService.getCurrentUser();
        if (!article.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to modify this article");
        }
    }

    @Transactional
    private void trackRecentlyViewed(Article article) {
        User currentUser = userService.getCurrentUser();
        
        // Check if the article is already in recently viewed
        Optional<RecentlyViewedArticle> existingView = recentlyViewedRepository
                .findByUserAndArticle(currentUser, article);
        
        if (existingView.isPresent()) {
            // Update the viewed_at timestamp
            RecentlyViewedArticle viewed = existingView.get();
            viewed.setViewedAt(LocalDateTime.now());
            recentlyViewedRepository.save(viewed);
            return;
        }

        // Remove the oldest viewed article if we've reached the maximum
        long count = recentlyViewedRepository.countByUser(currentUser);
        if (count >= maxRecentArticles) {
            recentlyViewedRepository.deleteOldestByUser(currentUser);
        }
        
        // Add the new view
        RecentlyViewedArticle recentlyViewedArticle = new RecentlyViewedArticle();
        recentlyViewedArticle.setUser(currentUser);
        recentlyViewedArticle.setArticle(article);
        recentlyViewedArticle.setViewedAt(LocalDateTime.now());
        recentlyViewedRepository.save(recentlyViewedArticle);
    }

    private ArticleResponse convertToResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setContent(article.getContent());
        response.setAuthorUsername(article.getAuthor().getUsername());
        response.setCreatedAt(article.getCreatedAt());
        response.setUpdatedAt(article.getUpdatedAt());
        return response;
    }
} 
package com.cms.repository;

import com.cms.model.Article;
import com.cms.model.RecentlyViewedArticle;
import com.cms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RecentlyViewedArticleRepository extends JpaRepository<RecentlyViewedArticle, Long> {
    @Query("SELECT rva FROM RecentlyViewedArticle rva WHERE rva.user = :user ORDER BY rva.viewedAt DESC")
    Page<RecentlyViewedArticle> findRecentlyViewedByUser(@Param("user") User user, Pageable pageable);

    Optional<RecentlyViewedArticle> findByUserAndArticle(User user, Article article);

    long countByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecentlyViewedArticle rva WHERE rva.user = :user AND rva.viewedAt = (SELECT MIN(r.viewedAt) FROM RecentlyViewedArticle r WHERE r.user = :user)")
    void deleteOldestByUser(@Param("user") User user);

    void deleteByUserAndArticleId(User user, Long articleId);
} 
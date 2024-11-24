package ru.yandex.masterskaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.masterskaya.model.Like;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByReviewIdAndUserId(Long reviewId, Long userId);
    long countByReviewIdAndIsLikeTrue(Long reviewId);
    long countByReviewIdAndIsLikeFalse(Long reviewId);
    void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}

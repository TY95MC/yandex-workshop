package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.exception.EntityNotFoundException;
import ru.yandex.masterskaya.model.Like;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.mapper.ReviewMapper;
import ru.yandex.masterskaya.repository.LikeRepository;
import ru.yandex.masterskaya.repository.ReviewRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    @Override
    public ReviewFullDto addLike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));

        if (review.getAuthorId() != userId) {
            Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
            if (existingLike.isEmpty()) {
                Like like = new Like();
                like.setReview(review);
                like.setUserId(userId);
                like.setLike(true);
                // like.setCreatedDateTime(LocalDateTime.now());
                likeRepository.save(like);
            } else {
                throw new ConflictException("Пользователь уже поставил лайк данному отзыву");
            }
            return getReviewWithLikesDislikes(review.getId());

        } else {
            throw new ConflictException("Автор отзыва не может ставить себе лайк");
        }
    }

    @Transactional
    @Override
    public ReviewFullDto removeLike(Long reviewId, Long userId) {
        Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
        if (existingLike.isPresent() && existingLike.get().isLike()) {
            likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
        }

        return getReviewWithLikesDislikes(reviewId);
    }

    @Transactional
    @Override
    public ReviewFullDto addDislike(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));

        if (review.getAuthorId() != userId) {
            Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
            if (existingLike.isPresent()) {
                if (existingLike.get().isLike()) {
                    likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
                } else {
                    //Dislike already exists, do nothing
                }
            } else {
                Like dislike = new Like();
                dislike.setReview(review);
                dislike.setUserId(userId);
                dislike.setLike(false);
                // dislike.setCreatedDateTime(LocalDateTime.now());
                likeRepository.save(dislike);
            }
        } else {
            throw new ConflictException("Автор отзыва не может ставить себе дизлайк");
        }

        return getReviewWithLikesDislikes(review.getId());
    }

//    Если пользователь уже поставил лайк, а теперь ставит дизлайк, то лайк просто удаляется, а дизлайк не сохраняется,
//    аналогично наоборот.

    @Transactional
    @Override
    public ReviewFullDto removeDislike(Long reviewId, Long userId) {
        Optional<Like> existingLike = likeRepository.findByReviewIdAndUserId(reviewId, userId);
        if (existingLike.isPresent() && !existingLike.get().isLike()) {
            likeRepository.deleteByReviewIdAndUserId(reviewId, userId);
        }
        return getReviewWithLikesDislikes(reviewId);
    }

    @Transactional
    @Override
    public ReviewFullDto getReviewWithLikesDislikes(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден"));
        ReviewFullDto reviewFullDto = reviewMapper.toReviewFullDto(review);
        reviewFullDto.setNumberOfLikes(likeRepository.countByReviewIdAndIsLikeTrue(review.getId()));
        reviewFullDto.setNumberOfDisLikes(likeRepository.countByReviewIdAndIsLikeFalse(review.getId()));
        return reviewFullDto;
    }
}


package ru.yandex.masterskaya.model.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-26T01:36:12+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Amazon.com Inc.)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review toReview(CreateReviewDto dto) {
        if ( dto == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.authorId( dto.getAuthorId() );
        review.username( dto.getUsername() );
        review.title( dto.getTitle() );
        review.content( dto.getContent() );
        review.mark( dto.getMark() );
        review.eventId( dto.getEventId() );

        return review.build();
    }

    @Override
    public ReviewDto toReviewDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId( review.getId() );
        reviewDto.setUsername( review.getUsername() );
        reviewDto.setTitle( review.getTitle() );
        reviewDto.setContent( review.getContent() );
        reviewDto.setCreatedDateTime( review.getCreatedDateTime() );
        reviewDto.setUpdatedDateTime( review.getUpdatedDateTime() );
        reviewDto.setMark( review.getMark() );
        reviewDto.setEventId( review.getEventId() );

        return reviewDto;
    }

    @Override
    public ReviewFullDto toReviewFullDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewFullDto reviewFullDto = new ReviewFullDto();

        reviewFullDto.setId( review.getId() );
        reviewFullDto.setAuthorId( review.getAuthorId() );
        reviewFullDto.setUsername( review.getUsername() );
        reviewFullDto.setTitle( review.getTitle() );
        reviewFullDto.setContent( review.getContent() );
        reviewFullDto.setCreatedDateTime( review.getCreatedDateTime() );
        reviewFullDto.setUpdatedDateTime( review.getUpdatedDateTime() );
        reviewFullDto.setMark( review.getMark() );
        reviewFullDto.setEventId( review.getEventId() );

        return reviewFullDto;
    }

    @Override
    public List<ReviewFullDto> toListReviewFullDto(List<Review> reviews) {
        if ( reviews == null ) {
            return null;
        }

        List<ReviewFullDto> list = new ArrayList<ReviewFullDto>( reviews.size() );
        for ( Review review : reviews ) {
            list.add( toReviewFullDto( review ) );
        }

        return list;
    }
}

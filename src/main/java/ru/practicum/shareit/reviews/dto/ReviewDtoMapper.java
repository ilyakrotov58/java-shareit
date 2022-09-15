package ru.practicum.shareit.reviews.dto;

import ru.practicum.shareit.reviews.model.Review;

public class ReviewDtoMapper {

    public static ReviewDto toDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getOwnerId(),
                review.getUserId(),
                review.isTaskWasCompleted(),
                review.getReviewText(),
                review.isPositive());
    }

    public static Review fromDto(ReviewDto reviewDto) {
        return new Review(
                reviewDto.getId(),
                reviewDto.getOwnerId(),
                reviewDto.getUserId(),
                reviewDto.isTaskWasCompleted(),
                reviewDto.getReviewText(),
                reviewDto.isPositive()
        );
    }
}

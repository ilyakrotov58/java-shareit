package ru.practicum.shareit.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ReviewDto {

    private long id;

    @NotNull
    private long ownerId;

    @NotNull
    private long userId;

    @NotNull
    private boolean isTaskWasCompleted;

    @NotBlank(message = "Text of review can't be null or empty")
    private String reviewText;

    @NotNull
    private boolean isPositive;
}

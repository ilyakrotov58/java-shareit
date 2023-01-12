package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {

    private long id;

    @NotNull
    private long userId;

    @NotBlank(message = "Item name can't be null or empty")
    private String name;

    @NotBlank(message = "Item description can't be null or empty")
    @Size(max = 200, message = "Description of item is more than 200 symbols")
    private String description;

    @NotNull
    private Boolean available;

    @Nullable
    private Booking lastBooking;

    @Nullable
    private Booking nextBooking;

    private List<Comment> comments;

    @Data
    @AllArgsConstructor
    public static class Booking {

        private long id;

        private long bookerId;

        @FutureOrPresent
        private LocalDateTime start;

        @Future
        private LocalDateTime end;

        private long itemId;
    }

    @Data
    @AllArgsConstructor
    public static class Comment {

        @NotNull
        private long id;

        @NotNull
        private String text;

        @NotNull
        private String authorName;

        @NotNull
        private LocalDateTime createdAt;
    }
}

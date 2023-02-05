package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoExt {

    private long id;

    private User booker;

    private Item item;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private BookingStatus status;

    private long itemId;

    @Data
    @AllArgsConstructor
    public static class Item {

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
        private BookingDtoExt lastBooking;

        @Nullable
        private BookingDtoExt nextBooking;

        private List<Comment> comments;

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

    @Data
    @AllArgsConstructor
    public static class User {

        private long id;

        @NotBlank
        @Email(message = "Invalid format of email")
        private String email;

        private String name;

        private String lastName;
    }
}

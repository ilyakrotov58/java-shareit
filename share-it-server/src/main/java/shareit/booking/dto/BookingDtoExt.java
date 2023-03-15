package shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoExt {

    private long id;

    private User booker;

    private Item item;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private long itemId;

    @Data
    @AllArgsConstructor
    public static class Item {

        private long id;

        private long userId;

        private String name;

        private String description;

        private Boolean available;

        private BookingDtoExt lastBooking;

        private BookingDtoExt nextBooking;

        private List<Comment> comments;

        @Data
        @AllArgsConstructor
        public static class Comment {

            private long id;

            private String text;

            private String authorName;

            private LocalDateTime created;
        }
    }

    @Data
    @AllArgsConstructor
    public static class User {

        private long id;

        private String email;

        private String name;

        private String lastName;
    }
}

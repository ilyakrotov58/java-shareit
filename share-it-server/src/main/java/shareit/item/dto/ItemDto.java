package shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private long id;

    private long userId;

    private String name;

    private String description;

    private Boolean available;

    private Booking lastBooking;

    private Booking nextBooking;

    private Long requestId;

    private List<Comment> comments;

    @Data
    @AllArgsConstructor
    public static class Booking {

        private long id;

        private long bookerId;

        private LocalDateTime start;

        private LocalDateTime end;

        private long itemId;
    }

    @Data
    @AllArgsConstructor
    public static class Comment {

        private long id;

        private String text;

        private String authorName;

        private LocalDateTime createdAt;
    }
}

package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    private BookingDto lastBooking;

    @Nullable
    private BookingDto nextBooking;

    private List<CommentDto> comments;
}

package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoExt {

    private long id;

    private User booker;

    private Item item;

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private BookingStatus status;

    private long itemId;
}

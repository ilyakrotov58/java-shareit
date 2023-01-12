package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {

    private long id;

    private long bookerId;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private long itemId;
}

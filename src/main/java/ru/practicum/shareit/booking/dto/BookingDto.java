package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BookingDto {

    private long id;

    @NotNull
    private int ownerId;

    @NotNull
    private int requestUserId;

    @NotNull
    private boolean isConfirmed;
}

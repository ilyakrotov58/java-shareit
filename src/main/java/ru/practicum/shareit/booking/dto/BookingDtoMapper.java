package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingDtoMapper {

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getOwnerId(),
                booking.getRequestUserId(),
                booking.isConfirmed()
        );
    }

    public static Booking fromDto(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getOwnerId(),
                bookingDto.getRequestUserId(),
                bookingDto.isConfirmed()
        );
    }
}

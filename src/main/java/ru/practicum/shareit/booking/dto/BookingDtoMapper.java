package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingDtoMapper {

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );
    }

    public static Booking fromDto(BookingDto bookingDto) {
        return new Booking(
                0,
                null,
                null,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null
        );
    }

    public static BookingDtoExt toExtDto(Booking booking) {
        return new BookingDtoExt(
                booking.getId(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getItem().getId());
    }
}

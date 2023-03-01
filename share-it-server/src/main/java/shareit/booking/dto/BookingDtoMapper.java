package shareit.booking.dto;

import shareit.booking.model.Booking;

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

        var bookingDtoExtUser = new BookingDtoExt.User(
                booking.getBooker().getId(),
                booking.getBooker().getEmail(),
                booking.getBooker().getName(),
                booking.getBooker().getLastName()
        );

        var bookingDtoExtItem = new BookingDtoExt.Item(
                booking.getItem().getId(),
                booking.getItem().getUserId(),
                booking.getItem().getName(),
                booking.getItem().getDescription(),
                booking.getItem().getAvailable(),
                null,
                null,
                null);

        return new BookingDtoExt(
                booking.getId(),
                bookingDtoExtUser,
                bookingDtoExtItem,
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getItem().getId());
    }
}

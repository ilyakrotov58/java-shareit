package ru.practicum.shareit.jsonTests.bookings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExt;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingsJsonTests {

    @Autowired
    private JacksonTester<BookingDto> jsonBookingDto;
    @Autowired
    private JacksonTester<BookingDtoExt> jsonBookingDtoExt;

    @Test
    void testBookingDto() throws Exception {

        // Arrange
        var bookingDto = BookingDtoMapper.toDto(EntityGenerator.createBooking());

        // Act
        JsonContent<BookingDto> result = jsonBookingDto.write(bookingDto);

        // Assert
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo((int) bookingDto.getId());

        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo((int) bookingDto.getBookerId());

        assertThat(result).extractingJsonPathStringValue("$.start")
                .contains(bookingDto.getStart().toString());

        assertThat(result).extractingJsonPathStringValue("$.end")
                .contains(bookingDto.getEnd().toString());

        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo((int) bookingDto.getItemId());
    }

    @Test
    void testBookingDtoExt() throws Exception {

        // Arrange
        var bookingDtoExt = BookingDtoMapper.toExtDto(EntityGenerator.createBooking());

        // Act
        JsonContent<BookingDtoExt> result = jsonBookingDtoExt.write(bookingDtoExt);

        // Assert
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo((int) bookingDtoExt.getId());

        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo(bookingDtoExt.getBooker().getName());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo((int) bookingDtoExt.getBooker().getId());
        assertThat(result).extractingJsonPathStringValue("$.booker.lastName")
                .isEqualTo(bookingDtoExt.getBooker().getLastName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo(bookingDtoExt.getBooker().getEmail());

        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo((int) bookingDtoExt.getItem().getId());
        assertThat(result).extractingJsonPathNumberValue("$.item.userId")
                .isEqualTo((int) bookingDtoExt.getItem().getUserId());
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo(bookingDtoExt.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(bookingDtoExt.getItem().getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .isEqualTo(bookingDtoExt.getItem().getAvailable());
        assertThat(result).extractingJsonPathValue("$.item.lastBooking")
                .isEqualTo(bookingDtoExt.getItem().getLastBooking());
        assertThat(result).extractingJsonPathValue("$.item.nextBooking")
                .isEqualTo(bookingDtoExt.getItem().getNextBooking());
        assertThat(result).extractingJsonPathValue("$.item.comments")
                .isEqualTo(bookingDtoExt.getItem().getComments());

        assertThat(result).extractingJsonPathStringValue("$.start")
                .contains(bookingDtoExt.getStart().toString());

        assertThat(result).extractingJsonPathStringValue("$.end")
                .contains(bookingDtoExt.getEnd().toString());

        assertThat(result).extractingJsonPathStringValue("$.status")
                .contains(bookingDtoExt.getStatus().toString());

        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo((int) bookingDtoExt.getItemId());
    }
}
package ru.practicum.shareit.jsonTests.bookings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingsJsonTests {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {

        // Arrange
        var bookingDto = BookingDtoMapper.toDto(EntityGenerator.createBooking());

        // Act
        JsonContent<BookingDto> result = json.write(bookingDto);

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
}
package ru.practicum.shareit.booking.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExt;

import javax.validation.Valid;
import java.util.List;

public interface IBookingService {

    BookingDtoExt add(@Valid
                      @RequestBody BookingDto bookingDto,
                      @RequestHeader("X-Sharer-User-Id") long userId);

    BookingDtoExt approve(@Valid @PathVariable long bookingId,
                          @RequestParam boolean approved,
                          @RequestHeader("X-Sharer-User-Id") long userId);

    BookingDtoExt get(@PathVariable long bookingId,
                      @RequestHeader("X-Sharer-User-Id") long userId);

    List<BookingDtoExt> getAllBookings(@RequestParam String state,
                                       @RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(defaultValue = "0") long index,
                                       @RequestParam(defaultValue = "10") long size);


    List<BookingDtoExt> getAllBookingsByItems(@RequestParam String state,
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "0") long index,
                                              @RequestParam(defaultValue = "10") long size);
}

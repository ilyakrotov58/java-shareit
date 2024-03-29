package shareit.booking.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import shareit.booking.dto.BookingDto;
import shareit.booking.dto.BookingDtoExt;

import java.util.List;

public interface IBookingService {

    BookingDtoExt add(@RequestBody BookingDto bookingDto,
                      @RequestHeader("X-Sharer-User-Id") long userId);

    BookingDtoExt approve(@PathVariable long bookingId,
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

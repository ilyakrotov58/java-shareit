package shareit.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shareit.booking.dto.BookingDto;
import shareit.booking.dto.BookingDtoExt;
import shareit.booking.service.IBookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Tag(name = "Bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping
    @Operation(summary = "Add booking")
    public BookingDtoExt add(@RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "Approve or reject booking")
    public BookingDtoExt approve(@PathVariable long bookingId,
                                 @RequestParam boolean approved,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking info")
    public BookingDtoExt get(@PathVariable long bookingId,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "Get all user bookings")
    public List<BookingDtoExt> getAllBookings(@RequestParam(defaultValue = "ALL") String state,
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "0")
                                              long from,
                                              @RequestParam(defaultValue = "10") long size) {
        return bookingService.getAllBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "Get all user bookings by user items")
    public List<BookingDtoExt> getAllBookingsByItems(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "0")
                                                     long from,
                                                     @RequestParam(defaultValue = "10") long size) {
        return bookingService.getAllBookingsByItems(state, userId, from, size);
    }
}

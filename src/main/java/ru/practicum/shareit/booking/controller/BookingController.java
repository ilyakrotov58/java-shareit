package ru.practicum.shareit.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoExt;
import ru.practicum.shareit.booking.service.IBookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Tag(name = "Bookings")
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping
    @Operation(summary = "Add booking")
    public BookingDtoExt add(@Valid @RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "Approve or reject booking")
    public BookingDtoExt approve(@Valid @PathVariable long bookingId,
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
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllBookings(state, userId);
    }

    @GetMapping("/owner")
    @Operation(summary = "Get all user bookings by user items")
    public List<BookingDtoExt> getAllBookingsByItems(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllBookingsByItems(state, userId);
    }
}

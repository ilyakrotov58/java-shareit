package shareit.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.booking.dto.BookingDto;
import shareit.booking.model.BookingStatus;
import shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @Operation(summary = "Add booking")
    public ResponseEntity<Object> add(@Valid @RequestBody BookingDto bookingDto,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "Approve or reject booking")
    public ResponseEntity<Object> approve(@Valid @PathVariable long bookingId,
                                          @RequestParam boolean approved,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approve(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Get booking info")
    public ResponseEntity<Object> get(@PathVariable long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "Get all user bookings")
    public ResponseEntity<Object> getAllBookings(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "0")
                                                 @PositiveOrZero long from,
                                                 @RequestParam(defaultValue = "10")
                                                 @Positive long size) {

        BookingStatus.isValid(state);

        return bookingService.getAllBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "Get all user bookings by user items")
    public ResponseEntity<Object> getAllBookingsByItems(@RequestParam(defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "0")
                                                        @PositiveOrZero long from,
                                                        @RequestParam(defaultValue = "10")
                                                        @Positive long size) {

        BookingStatus.isValid(state);

        return bookingService.getAllBookingsByItems(state, userId, from, size);
    }
}

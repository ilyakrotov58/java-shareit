package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exceptions.UnsupportedBookingStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BookingStatus {
    ALL, PAST, CURRENT, FUTURE, WAITING, REJECTED, APPROVED;

    public static void isValid(String status) {
        var result = getAll().stream().map(Enum::toString).anyMatch(b -> b.equals(status));


        if (!result) {
            throw new UnsupportedBookingStatusException("{\"error\":\"Unknown state: " + status + "\"}");
        }
    }

    private static List<BookingStatus> getAll() {
        return Arrays.stream(BookingStatus.values()).collect(Collectors.toList());
    }
}

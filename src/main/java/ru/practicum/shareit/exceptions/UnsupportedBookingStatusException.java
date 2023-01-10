package ru.practicum.shareit.exceptions;

public class UnsupportedBookingStatusException extends RuntimeException {

    public UnsupportedBookingStatusException(String message) {
        super(message);
    }
}

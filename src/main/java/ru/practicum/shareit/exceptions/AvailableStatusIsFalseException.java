package ru.practicum.shareit.exceptions;

public class AvailableStatusIsFalseException extends RuntimeException {

    public AvailableStatusIsFalseException(String message) {
        super(message);
    }
}

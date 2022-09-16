package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookingStorage implements IBookingStorage {

    private final Map<Long, Booking> inMemoryBookingStorage = new HashMap<>();

    private long id = 0;

}

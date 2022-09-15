package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

import java.util.HashMap;

@Component
public class BookingStorage implements IBookingStorage {

    private final HashMap<Long, Booking> inMemoryBookingStorage = new HashMap<>();

    private long id = 0;

}

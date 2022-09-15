package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.storage.IBookingStorage;

@Slf4j
@Service
public class BookingService implements IBookingService {

    @Autowired
    private IBookingStorage storage;
}

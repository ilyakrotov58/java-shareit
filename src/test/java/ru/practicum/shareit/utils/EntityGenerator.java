package ru.practicum.shareit.utils;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EntityGenerator {

    public static Booking createBooking() {
        return new Booking(
                0,
                createUser(),
                createItem(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.APPROVED
        );
    }

    public static User createUser() {
        return new User(
                0,
                DataGenerator.generateEmail(),
                DataGenerator.generateRandomString(4),
                DataGenerator.generateRandomString(5)
        );
    }

    public static Item createItem() {
        return new Item(
                0,
                0,
                DataGenerator.generateRandomString(4),
                DataGenerator.generateRandomString(10),
                true,
                0L,
                new ArrayList<>()
        );
    }

    public static Comment createComment() {
        return new Comment(
                0,
                DataGenerator.generateRandomString(5),
                createUser(),
                createItem(),
                LocalDateTime.now()
        );
    }

    public static ItemRequest createRequest() {
        var items = new ArrayList<Item>();
        items.add(createItem());

        return new ItemRequest(
                0,
                DataGenerator.generateRandomString(5),
                LocalDateTime.now(),
                0L,
                items
        );
    }
}

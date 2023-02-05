package ru.practicum.shareit.integrationTests.bookings;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItApp.class)
public class BookingIntTests {

    @Autowired
    IBookingRepository bookingRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ICommentRepository commentRepository;

    @Autowired
    IItemRepository itemRepository;

    @Autowired
    IBookingService bookingService;

    @BeforeEach
    void clearDbBeforeTest() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllBookings_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userInDb = userRepository.save(user);

        var item = EntityGenerator.createItem();
        item.setUserId(userInDb.getId());
        item.setRequestId(null);

        var itemInDb = itemRepository.save(item);

        var booking = EntityGenerator.createBooking();
        booking.setBooker(userInDb);
        booking.setItem(itemInDb);

        var savedBooking = bookingRepository.save(booking);
        var expectingBooking = BookingDtoMapper.toExtDto(savedBooking);

        // Act
        var actualBooking = bookingService.getAllBookings(
                booking.getStatus().toString(),
                booking.getBooker().getId(),
                0,
                1);

        // Assert
        assertThat(actualBooking.size()).isEqualTo(1);

        assertThat(actualBooking.get(0))
                .hasFieldOrPropertyWithValue("id", expectingBooking.getId())
                .hasFieldOrPropertyWithValue("booker", expectingBooking.getBooker())
                .hasFieldOrPropertyWithValue("item", expectingBooking.getItem())
                .hasFieldOrPropertyWithValue("start", expectingBooking.getStart())
                .hasFieldOrPropertyWithValue("status", expectingBooking.getStatus());
    }

    @Test
    void approveBookings_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userInDb = userRepository.save(user);
        var secondUserInDb = userRepository.save(EntityGenerator.createUser());

        var item = EntityGenerator.createItem();
        item.setUserId(userInDb.getId());
        item.setRequestId(null);

        var itemInDb = itemRepository.save(item);

        var booking = EntityGenerator.createBooking();
        booking.setBooker(userInDb);
        booking.setItem(itemInDb);
        booking.setStatus(BookingStatus.WAITING);

        var savedBooking = bookingRepository.save(booking);

        // Act
        var actualBooking = bookingService.approve(
                savedBooking.getId(),
                true,
                secondUserInDb.getId());

        // Assert
        assertThat(actualBooking)
                .hasFieldOrPropertyWithValue("id", savedBooking.getId())
                .hasFieldOrPropertyWithValue("status", BookingStatus.APPROVED);
    }

    @AfterEach
    void clearDb() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}

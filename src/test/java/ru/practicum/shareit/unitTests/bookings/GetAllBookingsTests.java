package ru.practicum.shareit.unitTests.bookings;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingDtoExt;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnsupportedBookingStatusException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.utils.EntityGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAllBookingsTests {

    private IUserRepository userRepository;
    private IItemRepository itemRepository;
    private IBookingService bookingService;
    private IBookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        bookingRepository = mock(IBookingRepository.class);
        userRepository = mock(IUserRepository.class);
        itemRepository = mock(IItemRepository.class);

        bookingService = new BookingService();
        ReflectionTestUtils.setField(bookingService, "userRepository", userRepository);
        ReflectionTestUtils.setField(bookingService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(bookingService, "bookingRepository", bookingRepository);
    }

    @ParameterizedTest(name = "Get all bookings")
    @ValueSource(strings = {"ALL", "FUTURE", "WAITING", "REJECTED", "APPROVED"})
    void getAllBookings_WithCorrectParams_ReturnsCorrectResponse(String bookingStatus) {

        // Arrange
        var booking = setUpBooking();
        booking.setStatus(BookingStatus.valueOf(bookingStatus));

        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        var listOfBookings = new ArrayList<Booking>();
        listOfBookings.add(booking);

        var expectedListOfBookings = new ArrayList<BookingDtoExt>();
        expectedListOfBookings.add(bookingDtoExt);

        Mockito
                .when(bookingRepository.getAll(Mockito.anyLong()))
                .thenReturn(listOfBookings);

        // Act
        var actualListOfBookings = bookingService.getAllBookings(
                bookingStatus,
                0,
                0,
                1);

        // Assert
        Assertions.assertEquals(expectedListOfBookings, actualListOfBookings);
    }

    @Test
    void getAllBookings_WithPastStatus_ReturnsCorrectResponse() {

        // Arrange
        var booking = setUpBooking();
        booking.setStatus(BookingStatus.PAST);
        booking.setEnd(LocalDateTime.now().minusDays(2));

        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        var listOfBookings = new ArrayList<Booking>();
        listOfBookings.add(booking);

        var expectedListOfBookings = new ArrayList<BookingDtoExt>();
        expectedListOfBookings.add(bookingDtoExt);

        Mockito
                .when(bookingRepository.getAll(Mockito.anyLong()))
                .thenReturn(listOfBookings);

        // Act
        var actualListOfBookings = bookingService.getAllBookings(
                BookingStatus.PAST.toString(),
                0,
                0,
                1);

        // Assert
        Assertions.assertEquals(expectedListOfBookings, actualListOfBookings);
    }

    @Test
    void getAllBookings_WithCurrentStatus_ReturnsCorrectResponse() {

        // Arrange
        var booking = setUpBooking();
        booking.setStatus(BookingStatus.CURRENT);
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStart(LocalDateTime.now().minusDays(2));

        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        var listOfBookings = new ArrayList<Booking>();
        listOfBookings.add(booking);

        var expectedListOfBookings = new ArrayList<BookingDtoExt>();
        expectedListOfBookings.add(bookingDtoExt);

        Mockito
                .when(bookingRepository.getAll(Mockito.anyLong()))
                .thenReturn(listOfBookings);

        // Act
        var actualListOfBookings = bookingService.getAllBookings(
                BookingStatus.CURRENT.toString(),
                0,
                0,
                1);

        // Assert
        Assertions.assertEquals(expectedListOfBookings, actualListOfBookings);
    }

    @Test
    void getAllBookings_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookings(
                    BookingStatus.APPROVED.toString(),
                    100,
                    0,
                    1);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }

    @Test
    void getAllBookings_WithNotValidState_ShouldThrowException() {

        // Arrange
        setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookings(
                    "Invalid status",
                    100,
                    0,
                    1);
        } catch (UnsupportedBookingStatusException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("{\"error\":\"Unknown state: Invalid status\"}");
    }

    @Test
    void getAllBookings_WithNotValidIndex_ShouldThrowException() {

        // Arrange
        var booking = setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookings(
                    BookingStatus.APPROVED.toString(),
                    booking.getBooker().getId(),
                    -1,
                    1);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Index should be >= 0");
    }

    @ParameterizedTest(name = "Incorrect size tests")
    @ValueSource(ints = {-1, 0})
    void getAllBookings_WithNotValidSize_ShouldThrowException(int incorrectSize) {

        // Arrange
        var booking = setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookings(
                    BookingStatus.APPROVED.toString(),
                    booking.getBooker().getId(),
                    0,
                    incorrectSize);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Size should be > 0");
    }

    private Booking setUpBooking() {
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        return booking;
    }
}

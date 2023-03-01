package shareit.unitTests.bookings;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.booking.dto.BookingDtoExt;
import shareit.booking.dto.BookingDtoMapper;
import shareit.booking.model.Booking;
import shareit.booking.model.BookingStatus;
import shareit.booking.repository.IBookingRepository;
import shareit.booking.service.BookingService;
import shareit.booking.service.IBookingService;
import shareit.exceptions.NotFoundException;
import shareit.item.repository.IItemRepository;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetBookingsByItemsTests {

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

    @Test
    void getAllBookingsByItems_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var booking = setUpBooking();
        var bookingDtoExt = BookingDtoMapper.toExtDto(booking);

        var listOfBookings = new ArrayList<Booking>();
        listOfBookings.add(booking);

        var expectedListOfBookings = new ArrayList<BookingDtoExt>();
        expectedListOfBookings.add(bookingDtoExt);

        Mockito
                .when(bookingRepository.getAllItemsBookings(Mockito.anyLong()))
                .thenReturn(listOfBookings);

        // Act
        var actualListOfBookings = bookingService.getAllBookingsByItems(
                BookingStatus.APPROVED.toString(),
                0,
                0,
                1);

        // Assert
        Assertions.assertEquals(expectedListOfBookings, actualListOfBookings);
    }

    @Test
    void getAllBookingsByItems_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookingsByItems(
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
    void getAllBookingsByItems_WithNotValidState_ShouldThrowException() {

        // Arrange
        setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookingsByItems(
                    "Invalid status",
                    100,
                    0,
                    1);
        } catch (IllegalArgumentException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();
    }

    @Test
    void getAllBookingsByItems_WithNotValidIndex_ShouldThrowException() {

        // Arrange
        var booking = setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.getAllBookingsByItems(
                    BookingStatus.APPROVED.toString(),
                    booking.getBooker().getId(),
                    -1,
                    1);
        } catch (IllegalArgumentException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();
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

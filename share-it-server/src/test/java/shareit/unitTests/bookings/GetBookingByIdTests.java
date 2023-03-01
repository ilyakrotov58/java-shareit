package shareit.unitTests.bookings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.booking.dto.BookingDtoMapper;
import shareit.booking.model.Booking;
import shareit.booking.repository.IBookingRepository;
import shareit.booking.service.BookingService;
import shareit.booking.service.IBookingService;
import shareit.exceptions.NotFoundException;
import shareit.item.repository.IItemRepository;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetBookingByIdTests {

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
    void getBookingById_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var booking = setUpBooking();
        var expectedBooking = BookingDtoMapper.toExtDto(booking);

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        // Act
        var actualBooking =
                bookingService.get(booking.getId(), 0);

        // Assert
        assertThat(actualBooking)
                .hasFieldOrPropertyWithValue("id", expectedBooking.getId())
                .hasFieldOrPropertyWithValue("booker", expectedBooking.getBooker())
                .hasFieldOrPropertyWithValue("item", expectedBooking.getItem())
                .hasFieldOrPropertyWithValue("start", expectedBooking.getStart())
                .hasFieldOrPropertyWithValue("status", expectedBooking.getStatus());
    }

    @Test
    void getBookingById_WithNotExistingId_ShouldThrowException() {

        // Arrange
        var booking = setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.get(0, booking.getBooker().getId());
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Booking with id = 0 can't be found");
    }

    @Test
    void getBookingById_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        RuntimeException exception = null;

        // Act
        try {
            bookingService.get(0, 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }

    @Test
    void getBookingById_WithUserIdHaveNoRights_ShouldThrowException() {

        // Arrange
        var booking = setUpBooking();

        Mockito
                .when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        RuntimeException exception = null;

        // Act
        try {
            bookingService.get(booking.getId(), 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("You have no rights to see this booking");
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

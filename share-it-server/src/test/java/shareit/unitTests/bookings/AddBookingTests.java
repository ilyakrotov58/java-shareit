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
import shareit.exceptions.AvailableStatusIsFalseException;
import shareit.exceptions.NotFoundException;
import shareit.exceptions.ValidateException;
import shareit.item.repository.IItemRepository;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddBookingTests {

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
    void addBooking_WithCorrectParams_ShouldReturnCorrectResponse() {

        // Arrange
        var booking = setUpBooking();
        var expectedBooking = BookingDtoMapper.toExtDto(booking);

        Mockito
                .when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(booking);

        // Act
        var actualBooking =
                bookingService.add(BookingDtoMapper.toDto(booking), 2);

        // Assert
        assertThat(actualBooking)
                .hasFieldOrPropertyWithValue("id", expectedBooking.getId())
                .hasFieldOrPropertyWithValue("booker", expectedBooking.getBooker())
                .hasFieldOrPropertyWithValue("item", expectedBooking.getItem())
                .hasFieldOrPropertyWithValue("start", expectedBooking.getStart())
                .hasFieldOrPropertyWithValue("status", expectedBooking.getStatus());
    }

    @Test
    void addBooking_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var booking = BookingDtoMapper.toDto(EntityGenerator.createBooking());

        RuntimeException exception = null;

        // Act
        try {
            bookingService.add(booking, 0);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 0 can't be found");
    }

    @Test
    void addBooking_WithNotExistingItem_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(item);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        RuntimeException exception = null;

        // Act
        try {
            bookingService.add(BookingDtoMapper.toDto(booking), 2);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Item with id = 0 can't be found");
    }

    @Test
    void addBooking_OwnerTryingToAddItem_ShouldThrowException() {

        // Arrange
        var booking = setUpBooking();

        RuntimeException exception = null;

        // Act
        try {
            bookingService.add(BookingDtoMapper.toDto(booking), booking.getBooker().getId());
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User can't creat booking for his own item");
    }

    @Test
    void addBooking_WithNotAvailableItem_ShouldThrowException() {

        // Arrange
        var booking = setUpBooking();
        booking.getItem().setAvailable(false);

        RuntimeException exception = null;

        // Act
        try {
            bookingService.add(BookingDtoMapper.toDto(booking), 2);
        } catch (AvailableStatusIsFalseException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Item with id = 0 is not available");
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

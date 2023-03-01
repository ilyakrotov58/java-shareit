package shareit.jpaTests.bookings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shareit.booking.repository.IBookingRepository;
import shareit.item.repository.IItemRepository;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingJpaTests {

    @Autowired
    private IBookingRepository bookingRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IItemRepository itemRepository;

    @Test
    void getAllBookings_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemInDb = itemRepository.save(item);

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(itemInDb);

        var expectedBooking = bookingRepository.save(booking);

        // Act
        var actualBooking = bookingRepository.getAll(booking.getBooker().getId());

        // Assert
        assertThat(actualBooking).hasSize(1);

        assertThat(actualBooking.get(0).getId()).isEqualTo(expectedBooking.getId());
    }

    @Test
    void getAllBookingsByItems_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemInDb = itemRepository.save(item);

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(itemInDb);

        var expectedBooking = bookingRepository.save(booking);

        // Act
        var actualBooking = bookingRepository.getAllByItemId(booking.getItem().getId());

        // Assert
        assertThat(actualBooking).hasSize(1);

        assertThat(actualBooking.get(0).getId()).isEqualTo(expectedBooking.getId());
    }

    @Test
    void getAllUsersBookings_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = userRepository.save(EntityGenerator.createUser());
        var item = EntityGenerator.createItem();
        item.setRequestId(null);
        item.setUserId(user.getId());
        var itemInDb = itemRepository.save(item);

        var booking = EntityGenerator.createBooking();
        booking.setBooker(user);
        booking.setItem(itemInDb);

        var expectedBooking = bookingRepository.save(booking);

        // Act
        var actualBooking = bookingRepository.getAllItemsBookings(booking.getBooker().getId());

        // Assert
        assertThat(actualBooking).hasSize(1);

        assertThat(actualBooking.get(0).getId()).isEqualTo(expectedBooking.getId());
    }
}

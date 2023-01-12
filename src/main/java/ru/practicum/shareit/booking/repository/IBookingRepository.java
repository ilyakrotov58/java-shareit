package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM bookings " +
            "WHERE bookings.booker_id = (?1)" +
            "ORDER BY bookings.start_date DESC", nativeQuery = true)
    List<Booking> getAll(long userId);

    @Query("SELECT booking FROM Booking booking " +
            "WHERE booking.item.id IN (" +
            "SELECT item.id FROM Item item " +
            "WHERE item.userId = ?1) " +
            "ORDER BY booking.start DESC")
    List<Booking> getAllItemsBookings(long userId);

    List<Booking> getAllByItemId(long itemId);
}

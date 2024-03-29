package shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareit.booking.dto.BookingDto;
import shareit.booking.dto.BookingDtoExt;
import shareit.booking.dto.BookingDtoMapper;
import shareit.booking.model.Booking;
import shareit.booking.model.BookingStatus;
import shareit.booking.repository.IBookingRepository;
import shareit.exceptions.AvailableStatusIsFalseException;
import shareit.exceptions.NotFoundException;
import shareit.exceptions.ValidateException;
import shareit.item.repository.IItemRepository;
import shareit.user.repository.IUserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class BookingService implements IBookingService {

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoExt add(BookingDto bookingDto, long userId) {

        checkIfUserExists(userId);

        if (itemRepository.findById(bookingDto.getItemId()).isEmpty()) {
            throw new NotFoundException(String.format("Item with id = %s can't be found", bookingDto.getItemId()));
        }

        var booking = BookingDtoMapper.fromDto(bookingDto);

        booking.setBooker(userRepository.findById(userId).get());
        booking.setItem(itemRepository.findById(bookingDto.getItemId()).get());
        booking.setStatus(BookingStatus.WAITING);

        if (booking.getItem().getUserId() == userId) {
            throw new NotFoundException("User can't creat booking for his own item");
        }

        if (!itemRepository.findById(booking.getItem().getId()).get().getAvailable()) {
            throw new AvailableStatusIsFalseException(String.format(
                    "Item with id = %s is not available", booking.getItem().getId()));
        }

        booking = bookingRepository.save(booking);

        log.info(String.format("Booking with id = %s was added", booking.getId()));

        return BookingDtoMapper.toExtDto(booking);
    }

    @Override
    @Transactional
    public BookingDtoExt approve(long bookingId, boolean approved, long userId) {
        var booking = bookingRepository.findById(bookingId);

        checkIfUserExists(userId);

        if (booking.isEmpty()) {
            throw new NotFoundException(String.format("Booking with id = %s can't be found", bookingId));
        }

        if (booking.get().getBooker().getId() == userId) {
            throw new NotFoundException("Booker can't change status");
        }

        if (approved && booking.get().getStatus() != BookingStatus.APPROVED) {
            booking.get().setStatus(BookingStatus.APPROVED);
            bookingRepository.save(booking.get());
        } else if (approved && booking.get().getStatus() == BookingStatus.APPROVED) {
            throw new ValidateException("Status of booking is already APPROVED");
        } else {
            booking.get().setStatus(BookingStatus.REJECTED);
        }

        return BookingDtoMapper.toExtDto(booking.get());
    }

    @Override
    public BookingDtoExt get(long bookingId, long userId) {

        checkIfUserExists(userId);
        var booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException(String.format("Booking with id = %s can't be found", bookingId));
        } else if (booking.get().getBooker().getId() != userId &&
                booking.get().getItem().getUserId() != userId) {
            throw new NotFoundException("You have no rights to see this booking");
        }

        return BookingDtoMapper.toExtDto(booking.get());
    }

    @Override
    public List<BookingDtoExt> getAllBookings(String state, long userId, long index, long size) {

        checkIfUserExists(userId);

        var convertedStatus = BookingStatus.valueOf(state);
        var userBookings = bookingRepository.getAll(userId);

        return getBookingsByState(userBookings, convertedStatus)
                .stream()
                .skip(index)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoExt> getAllBookingsByItems(String state, long userId, long index, long size) {

        checkIfUserExists(userId);

        var convertedStatus = BookingStatus.valueOf(state);

        var allBookings = bookingRepository.getAllItemsBookings(userId);


        return getBookingsByState(allBookings, convertedStatus)
                .stream()
                .skip(index)
                .limit(size)
                .collect(Collectors.toList());
    }

    private List<BookingDtoExt> getBookingsByState(List<Booking> allBookings, BookingStatus convertedStatus) {

        var result = new ArrayList<BookingDtoExt>();

        if (convertedStatus == BookingStatus.PAST) {
            result = allBookings
                    .stream()
                    .filter(bookingDto -> bookingDto.getEnd().isBefore(LocalDateTime.now()))
                    .map(BookingDtoMapper::toExtDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (convertedStatus == BookingStatus.CURRENT) {
            result = allBookings
                    .stream()
                    .filter(bookingDto -> bookingDto.getStart().isBefore(LocalDateTime.now())
                            && bookingDto.getEnd().isAfter(LocalDateTime.now()))
                    .map(BookingDtoMapper::toExtDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (convertedStatus == BookingStatus.FUTURE) {
            result = allBookings
                    .stream()
                    .filter(bookingDto -> bookingDto.getStart().isAfter(LocalDateTime.now()))
                    .map(BookingDtoMapper::toExtDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (convertedStatus == BookingStatus.WAITING) {
            result = allBookings
                    .stream()
                    .filter(bookingDto -> bookingDto.getStatus() == BookingStatus.WAITING)
                    .map(BookingDtoMapper::toExtDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (convertedStatus == BookingStatus.REJECTED) {
            result = allBookings
                    .stream()
                    .filter(bookingDto -> bookingDto.getStatus() == BookingStatus.REJECTED)
                    .map(BookingDtoMapper::toExtDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            result = allBookings
                    .stream()
                    .map(BookingDtoMapper::toExtDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return result;
    }

    private void checkIfUserExists(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }
    }
}

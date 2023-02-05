package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserIsNotOwnerException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.repository.IUserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ItemService implements IItemService {

    @Autowired
    private IItemRepository itemRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Transactional
    @Override
    public ItemDto add(ItemDto itemDto, long userId) {

        checkIfUserExists(userId);

        var item = ItemDtoMapper.fromDto(itemDto);
        item.setUserId(userId);
        var addedItem = itemRepository.save(item);
        log.info(String.format("Item with id =%s was added", addedItem.getId()));

        return ItemDtoMapper.toDto(addedItem);
    }

    @Transactional
    @Override
    public ItemDto edit(ItemDto itemDto, long itemId, long userId) {

        var item = ItemDtoMapper.fromDto(itemDto);
        item.setId(itemId);
        item.setUserId(userId);

        checkIfUserExists(userId);
        checkIfItemExists(itemId);

        var oldItem = itemRepository.findById(itemId).get();

        if (oldItem.getUserId() != userId) {
            throw new UserIsNotOwnerException(String.format("User with id = %s is not owner", userId));
        }

        if (item.getName() == null) item.setName(oldItem.getName());
        if (item.getDescription() == null) item.setDescription(oldItem.getDescription());
        if (item.getAvailable() == null) item.setAvailable(oldItem.getAvailable());

        itemRepository.save(item);
        log.info(String.format("Item with id =%s was edited", item.getId()));

        return ItemDtoMapper.toDto(item);
    }

    @Override
    public ItemDto getById(long itemId, long userId) {
        var item = itemRepository.findById(itemId);

        checkIfItemExists(itemId);
        checkIfUserExists(userId);

        var itemDto = ItemDtoMapper.toDto(item.get());
        var bookingsForItem = bookingRepository.getAllItemsBookings(item.get().getUserId());

        setBookingsToItem(userId, itemDto, bookingsForItem);

        return itemDto;
    }

    @Override
    @Transactional
    public void deleteById(long itemId, long userId) {

        checkIfItemExists(itemId);
        checkIfUserExists(userId);

        itemRepository.deleteById(itemId);
        log.info(String.format("Item with id =%s was deleted", itemId));
    }

    @Override
    public List<ItemDto> getAllByText(String text, long index, long size) {

        checkIndex(index);
        checkSize(size);

        var itemsDto = new ArrayList<ItemDto>();
        if (text.isEmpty()) {
            return itemsDto;
        }

        var items = itemRepository.findAll();

        var selectedItems = items
                .stream()
                .filter(item -> item.getAvailable() && (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());

        for (Item item : selectedItems) {
            itemsDto.add(ItemDtoMapper.toDto(item));
        }

        return itemsDto
                .stream()
                .skip(index)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAll(long userId, long index, long size) {

        checkIndex(index);
        checkSize(size);
        checkIfUserExists(userId);

        var items = itemRepository.getAll(userId);
        var itemsDto = new ArrayList<ItemDto>();
        for (Item item : items) {
            itemsDto.add(ItemDtoMapper.toDto(item));
        }

        for (ItemDto itemDto : itemsDto) {
            var bookingsForItem = bookingRepository.getAllItemsBookings(userId);
            setBookingsToItem(userId, itemDto, bookingsForItem);
        }

        return itemsDto
                .stream()
                .sorted((o1, o2) -> o1.getId() < o2.getId() ? 0 : 1)
                .skip(index)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {

        checkIfUserExists(userId);
        checkIfItemExists(itemId);

        var user = userRepository.getReferenceById(userId);
        var item = itemRepository.getReferenceById(itemId);

        var comment = CommentDtoMapper.fromDto(commentDto, user, item);
        comment.setCreatedAt(LocalDateTime.now());

        if (comment.getText().isEmpty()) {
            throw new ValidateException("Text can't be empty");
        }

        var allBookingByItemId = bookingRepository.getAllByItemId(itemId);

        if (allBookingByItemId
                .stream()
                .noneMatch(booking -> booking.getStatus() != BookingStatus.WAITING
                        && booking.getStatus() != BookingStatus.REJECTED
                        && booking.getBooker().getId() == userId)) {
            throw new ValidateException("Can't add comment for item without bookings");
        }

        var isAllBookingsWithFutureStatus = allBookingByItemId
                .stream()
                .filter(booking -> booking.getItem().getId() == itemId
                        && booking.getBooker().getId() == userId)
                .collect(Collectors.toList());

        if (isAllBookingsWithFutureStatus
                .stream()
                .allMatch(booking -> booking.getStart().isAfter(comment.getCreatedAt()))) {
            throw new ValidateException("Can't add comment for future bookings");
        }

        var savedCommentDto = commentRepository.save(comment);
        log.info(String.format("Comment with id =%s was added", savedCommentDto.getId()));

        return CommentDtoMapper.toDto(savedCommentDto);
    }

    private void checkIfUserExists(long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }
    }

    private void checkIfItemExists(long itemId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException(String.format("Item with id = %s can't be found", itemId));
        }
    }

    private void setBookingsToItem(long userId, ItemDto itemDto, List<Booking> bookingsForItem) {
        if (bookingsForItem.size() != 0) {
            var filteredBookings = bookingsForItem
                    .stream()
                    .filter(booking -> booking.getStatus() != BookingStatus.REJECTED
                            && booking.getStatus() != BookingStatus.WAITING)
                    .filter(booking -> booking.getItem().getUserId() == userId)
                    .filter(booking -> booking.getItem().getId() == itemDto.getId())
                    .map(BookingDtoMapper::toDto)
                    .collect(Collectors.toList());

            var filteredBookingsItemDto = new ArrayList<ItemDto.Booking>();

            for (BookingDto bookingDto : filteredBookings) {
                var bookingItemDto = new ItemDto.Booking(
                        bookingDto.getId(),
                        bookingDto.getBookerId(),
                        bookingDto.getStart(),
                        bookingDto.getEnd(),
                        bookingDto.getItemId()
                );

                filteredBookingsItemDto.add(bookingItemDto);
            }

            if (filteredBookings.size() > 1) {
                itemDto.setLastBooking(filteredBookingsItemDto.get(filteredBookings.size() - 1));
                itemDto.setNextBooking(filteredBookingsItemDto.get(filteredBookings.size() - 2));
            } else if (filteredBookings.size() == 1) {
                itemDto.setNextBooking(filteredBookingsItemDto.get(0));
            }
        }
    }

    private void checkSize(long size) {
        if (size < 1) {
            throw new ValidateException("Size should be > 0");
        }
    }

    private void checkIndex(long index) {
        if (index < 0) {
            throw new ValidateException("Index should be >= 0");
        }
    }
}

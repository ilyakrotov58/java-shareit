package ru.practicum.shareit.unitTests.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.utils.EntityGenerator;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAllTests {

    private IUserRepository userRepository;
    private IItemRepository itemRepository;
    private IBookingRepository bookingRepository;
    private IItemService itemService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);
        itemRepository = mock(IItemRepository.class);
        bookingRepository = mock(IBookingRepository.class);

        itemService = new ItemService();
        ReflectionTestUtils.setField(itemService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "bookingRepository", bookingRepository);
    }

    @Test
    void getAllItems_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var item = EntityGenerator.createItem();
        var user = EntityGenerator.createUser();
        var booking = EntityGenerator.createBooking();

        var itemDto = ItemDtoMapper.toDto(item);
        itemDto.setNextBooking(new ItemDto.Booking(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()));

        var items = new ArrayList<Item>();
        items.add(item);

        var bookings = new ArrayList<Booking>();
        bookings.add(booking);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(bookingRepository.getAllItemsBookings(Mockito.anyLong()))
                .thenReturn(bookings);

        Mockito
                .when(itemRepository.getAll(Mockito.anyLong()))
                .thenReturn(items);

        // Act
        var actualItem = itemService.getAll(item.getUserId(), 0, 1);

        // Assert
        assertThat(actualItem).hasSize(1);

        assertThat(actualItem.get(0))
                .hasFieldOrPropertyWithValue("id", itemDto.getId())
                .hasFieldOrPropertyWithValue("userId", itemDto.getUserId())
                .hasFieldOrPropertyWithValue("name", itemDto.getName())
                .hasFieldOrPropertyWithValue("description", itemDto.getDescription())
                .hasFieldOrPropertyWithValue("available", itemDto.getAvailable())
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", itemDto.getNextBooking())
                .hasFieldOrPropertyWithValue("requestId", itemDto.getRequestId())
                .hasFieldOrPropertyWithValue("comments", new ArrayList<>());
    }

    @Test
    void getAllItems_WithNotExistingUser_ReturnsCorrectResponse() {

        // Arrange
        RuntimeException exception = null;

        // Act
        try {
            itemService.getAll(100, 0, 1);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }

    @ParameterizedTest(name = "Incorrect size tests")
    @ValueSource(ints = {-1, 0})
    void getAllItems_WithNotValidSize_ReturnsCorrectResponse(int incorrectSize) {

        // Arrange
        RuntimeException exception = null;

        var user = EntityGenerator.createUser();

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        try {
            itemService.getAll(user.getId(), 0, incorrectSize);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Size should be > 0");
    }

    @Test
    void getAllItems_WithNotValidIndex_ReturnsCorrectResponse() {

        // Arrange
        RuntimeException exception = null;

        var user = EntityGenerator.createUser();

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        try {
            itemService.getAll(user.getId(), -1, 1);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Index should be >= 0");
    }
}

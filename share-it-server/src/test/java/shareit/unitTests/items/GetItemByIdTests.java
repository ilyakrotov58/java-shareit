package shareit.unitTests.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.booking.model.Booking;
import shareit.booking.repository.IBookingRepository;
import shareit.exceptions.NotFoundException;
import shareit.item.dto.ItemDto;
import shareit.item.dto.ItemDtoMapper;
import shareit.item.model.Comment;
import shareit.item.repository.IItemRepository;
import shareit.item.service.IItemService;
import shareit.item.service.ItemService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetItemByIdTests {

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
    void getItemById_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var item = EntityGenerator.createItem();
        var comment = EntityGenerator.createComment();
        var commentList = new ArrayList<Comment>();
        commentList.add(comment);
        item.setComments(commentList);
        var user = EntityGenerator.createUser();
        var booking = EntityGenerator.createBooking();

        var itemDto = ItemDtoMapper.toDto(item);
        itemDto.setNextBooking(new ItemDto.Booking(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()));

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

        // Act
        var actualItem = itemService.getById(itemDto.getId(), user.getId());

        // Assert
        assertThat(actualItem)
                .hasFieldOrPropertyWithValue("id", itemDto.getId())
                .hasFieldOrPropertyWithValue("userId", itemDto.getUserId())
                .hasFieldOrPropertyWithValue("name", itemDto.getName())
                .hasFieldOrPropertyWithValue("description", itemDto.getDescription())
                .hasFieldOrPropertyWithValue("available", itemDto.getAvailable())
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", itemDto.getNextBooking())
                .hasFieldOrPropertyWithValue("requestId", itemDto.getRequestId())
                .hasFieldOrPropertyWithValue("comments", itemDto.getComments());
    }

    @Test
    void getItemById_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var item = EntityGenerator.createItem();

        RuntimeException exception = null;

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        // Act
        try {
            itemService.getById(item.getId(), 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }

    @Test
    void getItemById_WithNotExistingId_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        try {
            itemService.getById(100, user.getId());
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Item with id = 100 can't be found");
    }
}

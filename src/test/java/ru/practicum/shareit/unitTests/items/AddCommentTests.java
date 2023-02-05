package ru.practicum.shareit.unitTests.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.utils.EntityGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddCommentTests {

    private IUserRepository userRepository;
    private IItemRepository itemRepository;
    private ICommentRepository commentRepository;
    private IBookingRepository bookingRepository;
    private IItemService itemService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);
        itemRepository = mock(IItemRepository.class);
        commentRepository = mock(ICommentRepository.class);
        bookingRepository = mock(IBookingRepository.class);

        itemService = new ItemService();
        ReflectionTestUtils.setField(itemService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "commentRepository", commentRepository);
        ReflectionTestUtils.setField(itemService, "bookingRepository", bookingRepository);
    }

    @Test
    void addComment_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var item = EntityGenerator.createItem();
        var user = EntityGenerator.createUser();
        var comment = EntityGenerator.createComment();
        var booking = EntityGenerator.createBooking();
        booking.setStart(LocalDateTime.now().minusDays(1));

        var bookings = new ArrayList<Booking>();
        bookings.add(booking);

        var commentDto = CommentDtoMapper.toDto(comment);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito
                .when(bookingRepository.getAllByItemId(Mockito.anyLong()))
                .thenReturn(bookings);

        // Act
        var actualItem = itemService.addComment(user.getId(), item.getId(), commentDto);

        // Assert
        assertThat(actualItem)
                .hasFieldOrPropertyWithValue("id", commentDto.getId())
                .hasFieldOrPropertyWithValue("text", commentDto.getText())
                .hasFieldOrPropertyWithValue("authorName", commentDto.getAuthorName())
                .hasFieldOrPropertyWithValue("createdAt", commentDto.getCreatedAt());
    }

    @Test
    void addComment_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var item = EntityGenerator.createItem();
        var comment = EntityGenerator.createComment();
        var booking = EntityGenerator.createBooking();

        var bookings = new ArrayList<Booking>();
        bookings.add(booking);

        var commentDto = CommentDtoMapper.toDto(comment);

        RuntimeException exception = null;

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito
                .when(bookingRepository.getAllByItemId(Mockito.anyLong()))
                .thenReturn(bookings);

        // Act
        try {
            itemService.addComment(100, item.getId(), commentDto);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }

    @Test
    void addComment_WithNotExistingItem_ShouldThrowException() {

        // Arrange
        var comment = EntityGenerator.createComment();
        var booking = EntityGenerator.createBooking();
        var user = EntityGenerator.createUser();

        var bookings = new ArrayList<Booking>();
        bookings.add(booking);

        var commentDto = CommentDtoMapper.toDto(comment);

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito
                .when(bookingRepository.getAllByItemId(Mockito.anyLong()))
                .thenReturn(bookings);

        // Act
        try {
            itemService.addComment(user.getId(), 100, commentDto);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Item with id = 100 can't be found");
    }

    @Test
    void addComment_WithEmptyText_ShouldThrowException() {

        // Arrange
        var comment = EntityGenerator.createComment();
        comment.setText("");

        var booking = EntityGenerator.createBooking();
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var bookings = new ArrayList<Booking>();
        bookings.add(booking);

        var commentDto = CommentDtoMapper.toDto(comment);

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito
                .when(bookingRepository.getAllByItemId(Mockito.anyLong()))
                .thenReturn(bookings);

        // Act
        try {
            itemService.addComment(user.getId(), 100, commentDto);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Text can't be empty");
    }

    @Test
    void addComment_WhenItemHasNotBookings_ShouldThrowException() {

        // Arrange
        var comment = EntityGenerator.createComment();
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();

        var commentDto = CommentDtoMapper.toDto(comment);

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito
                .when(bookingRepository.getAllByItemId(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        // Act
        try {
            itemService.addComment(user.getId(), 100, commentDto);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Can't add comment for item without bookings");
    }

    @Test
    void addComment_WithFutureBooking_ShouldThrowException() {

        // Arrange
        var comment = EntityGenerator.createComment();
        var user = EntityGenerator.createUser();
        var item = EntityGenerator.createItem();
        var booking = EntityGenerator.createBooking();

        booking.setStatus(BookingStatus.FUTURE);

        var bookings = new ArrayList<Booking>();
        bookings.add(booking);

        var commentDto = CommentDtoMapper.toDto(comment);

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito
                .when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito
                .when(bookingRepository.getAllByItemId(Mockito.anyLong()))
                .thenReturn(bookings);

        // Act
        try {
            itemService.addComment(user.getId(), 100, commentDto);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Can't add comment for future bookings");
    }
}

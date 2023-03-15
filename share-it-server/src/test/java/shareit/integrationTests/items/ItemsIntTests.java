package shareit.integrationTests.items;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.ShareItServerApp;
import shareit.booking.repository.IBookingRepository;
import shareit.item.dto.ItemDtoMapper;
import shareit.item.repository.ICommentRepository;
import shareit.item.repository.IItemRepository;
import shareit.item.service.IItemService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItServerApp.class)
public class ItemsIntTests {

    @Autowired
    IBookingRepository bookingRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ICommentRepository commentRepository;

    @Autowired
    IItemRepository itemRepository;

    @Autowired
    IItemService itemService;

    @BeforeEach
    void clearDbBeforeTest() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllItemsByText_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userInDb = userRepository.save(user);

        var item = EntityGenerator.createItem();
        item.setUserId(userInDb.getId());
        item.setRequestId(null);

        var itemInDb = itemRepository.save(item);
        var itemDto = ItemDtoMapper.toDto(itemInDb);

        // Act
        var actualItem = itemService.getAllByText(item.getName(), 0, 1);

        // Assert
        assertThat(actualItem).hasSize(1);

        assertThat(actualItem.get(0))
                .hasFieldOrPropertyWithValue("id", itemDto.getId())
                .hasFieldOrPropertyWithValue("userId", itemDto.getUserId())
                .hasFieldOrPropertyWithValue("name", itemDto.getName())
                .hasFieldOrPropertyWithValue("description", itemDto.getDescription())
                .hasFieldOrPropertyWithValue("available", itemDto.getAvailable())
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", null)
                .hasFieldOrPropertyWithValue("requestId", itemDto.getRequestId())
                .hasFieldOrPropertyWithValue("comments", new ArrayList<>());
    }

    @AfterEach
    void clearDb() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        userRepository.deleteAll();
    }
}

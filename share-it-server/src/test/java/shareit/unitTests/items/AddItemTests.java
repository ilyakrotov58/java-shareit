package shareit.unitTests.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.exceptions.NotFoundException;
import shareit.item.dto.ItemDtoMapper;
import shareit.item.model.Item;
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
public class AddItemTests {

    private IUserRepository userRepository;
    private IItemRepository itemRepository;
    private IItemService itemService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);
        itemRepository = mock(IItemRepository.class);

        itemService = new ItemService();
        ReflectionTestUtils.setField(itemService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
    }

    @Test
    void addItem_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var item = EntityGenerator.createItem();
        var user = EntityGenerator.createUser();

        var itemDto = ItemDtoMapper.toDto(item);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        // Act
        var actualItem = itemService.add(itemDto, user.getId());

        // Assert
        assertThat(actualItem)
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

    @Test
    void addItem_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var itemDto = ItemDtoMapper.toDto(EntityGenerator.createItem());

        RuntimeException exception = null;

        // Act
        try {
            itemService.add(itemDto, 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }
}

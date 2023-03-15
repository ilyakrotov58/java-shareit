package shareit.unitTests.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.exceptions.NotFoundException;
import shareit.item.dto.ItemDtoMapper;
import shareit.item.repository.IItemRepository;
import shareit.item.service.IItemService;
import shareit.item.service.ItemService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteItemTests {

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
    void deleteItem_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var item = EntityGenerator.createItem();
        var itemDto = ItemDtoMapper.toDto(item);

        RuntimeException exception = null;

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        // Act
        try {
            itemService.deleteById(itemDto.getId(), 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }

    @Test
    void deleteItem_WithNotExistingId_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        try {
            itemService.deleteById(100, user.getId());
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Item with id = 100 can't be found");
    }
}

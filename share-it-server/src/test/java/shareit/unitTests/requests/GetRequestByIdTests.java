package shareit.unitTests.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.exceptions.NotFoundException;
import shareit.item.model.Item;
import shareit.item.repository.IItemRepository;
import shareit.request.dto.ItemRequestDto;
import shareit.request.dto.ItemRequestDtoMapper;
import shareit.request.repository.IItemRequestRepository;
import shareit.request.service.ItemRequestService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetRequestByIdTests {

    private IUserRepository userRepository;
    private IItemRepository itemRepository;
    private IItemRequestRepository itemRequestRepository;
    private ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);
        itemRepository = mock(IItemRepository.class);
        itemRequestRepository = mock(IItemRequestRepository.class);

        itemRequestService = new ItemRequestService();
        ReflectionTestUtils.setField(itemRequestService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRequestRepository", itemRequestRepository);
    }

    @Test
    void getRequestById_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var request = EntityGenerator.createRequest();
        var item = EntityGenerator.createItem();

        var itemList = new ArrayList<Item>();
        itemList.add(item);

        var itemRequestDto = new ItemRequestDto.Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId()
        );

        var itemListRequestDto = new ArrayList<ItemRequestDto.Item>();
        itemListRequestDto.add(itemRequestDto);

        var requestDto = ItemRequestDtoMapper.toDto(request);
        requestDto.setItems(itemListRequestDto);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(request));

        Mockito
                .when(itemRepository.getItemsByRequestId(Mockito.anyLong()))
                .thenReturn(itemList);

        // Act
        var actualRequest = itemRequestService.getById(request.getId(), user.getId());

        // Assert
        assertThat(actualRequest)
                .hasFieldOrPropertyWithValue("id", requestDto.getId())
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrPropertyWithValue("userId", requestDto.getUserId())
                .hasFieldOrPropertyWithValue("created", requestDto.getCreated())
                .hasFieldOrPropertyWithValue("items", requestDto.getItems());
    }

    @Test
    void getRequestById_WhenUserIsNotExist_ShouldThrowException() {

        // Arrange
        var request = EntityGenerator.createRequest();

        RuntimeException exception = null;

        // Act
        try {
            itemRequestService.getById(request.getId(), 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id 100 can't be found");
    }
}

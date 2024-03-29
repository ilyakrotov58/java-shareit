package shareit.unitTests.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.exceptions.NotFoundException;
import shareit.exceptions.ValidateException;
import shareit.item.model.Item;
import shareit.item.repository.IItemRepository;
import shareit.request.dto.ItemRequestDto;
import shareit.request.dto.ItemRequestDtoMapper;
import shareit.request.model.ItemRequest;
import shareit.request.repository.IItemRequestRepository;
import shareit.request.service.ItemRequestService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUsersRequestsTests {

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
    void getItemRequestOtherUsers_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var item = EntityGenerator.createItem();
        var user = EntityGenerator.createUser();
        var request = EntityGenerator.createRequest();

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
        requestDto.setUserId(user.getId());

        var requestList = new ArrayList<ItemRequest>();
        requestList.add(request);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRequestRepository.getItemRequestsOtherUsers(Mockito.anyLong()))
                .thenReturn(requestList);

        Mockito
                .when(itemRepository.getItemsByRequestId(Mockito.anyLong()))
                .thenReturn(itemList);

        Mockito
                .when(itemRepository.findAll())
                .thenReturn(itemList);

        // Act
        var actualRequests = itemRequestService.getUsersRequests(
                user.getId(),
                0,
                1);

        // Assert
        assertThat(actualRequests).hasSize(1);

        assertThat(actualRequests.get(0))
                .hasFieldOrPropertyWithValue("id", requestDto.getId())
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrPropertyWithValue("userId", requestDto.getUserId())
                .hasFieldOrPropertyWithValue("created", requestDto.getCreated())
                .hasFieldOrPropertyWithValue("items", requestDto.getItems());
    }

    @Test
    void getItemRequestOtherUsers_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        RuntimeException exception = null;

        // Act
        try {
            itemRequestService.getUsersRequests(100, 0, 1);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id 100 can't be found");
    }

    @ParameterizedTest(name = "Incorrect size tests")
    @ValueSource(ints = {-1, 0})
    void getItemRequestOtherUsers_WithIncorrectSize_ShouldThrowException(int incorrectSize) {

        // Arrange
        var user = EntityGenerator.createUser();

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        try {
            itemRequestService.getUsersRequests(100, 0, incorrectSize);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Size should be > 0");
    }

    @Test
    void getItemRequestOtherUsers_WithIncorrectIndex_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();

        RuntimeException exception = null;

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        try {
            itemRequestService.getUsersRequests(100, -1, 1);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("Index should be >= 0");
    }
}

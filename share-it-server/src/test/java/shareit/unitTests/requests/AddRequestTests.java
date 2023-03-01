package shareit.unitTests.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.exceptions.NotFoundException;
import shareit.exceptions.ValidateException;
import shareit.request.dto.ItemRequestDtoMapper;
import shareit.request.model.ItemRequest;
import shareit.request.repository.IItemRequestRepository;
import shareit.request.service.ItemRequestService;
import shareit.user.repository.IUserRepository;
import shareit.utils.EntityGenerator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddRequestTests {

    private IUserRepository userRepository;
    private IItemRequestRepository itemRequestRepository;
    private ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);
        itemRequestRepository = mock(IItemRequestRepository.class);

        itemRequestService = new ItemRequestService();
        ReflectionTestUtils.setField(itemRequestService, "userRepository", userRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRequestRepository", itemRequestRepository);
    }

    @Test
    void addRequest_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var request = EntityGenerator.createRequest();

        var requestDto = ItemRequestDtoMapper.toDto(request);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(request);

        // Act
        var actualRequest = itemRequestService.add(user.getId(), requestDto);

        // Assert
        assertThat(actualRequest)
                .hasFieldOrPropertyWithValue("id", requestDto.getId())
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrPropertyWithValue("userId", requestDto.getUserId())
                .hasFieldOrPropertyWithValue("items", requestDto.getItems());
    }

    @Test
    void addRequest_WithNotExistingUser_ShouldThrowException() {

        // Arrange
        var request = EntityGenerator.createRequest();
        var requestDto = ItemRequestDtoMapper.toDto(request);

        Mockito
                .when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(request);

        RuntimeException exception = null;

        // Act
        try {
            itemRequestService.add(100, requestDto);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id 100 can't be found");
    }

    @Test
    void addRequest_WithEmptyDescription_ShouldThrowException() {

        // Arrange
        var user = EntityGenerator.createUser();
        var request = EntityGenerator.createRequest();
        request.setDescription("");

        var requestDto = ItemRequestDtoMapper.toDto(request);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(request);

        RuntimeException exception = null;

        // Act
        try {
            itemRequestService.add(user.getId(), requestDto);
        } catch (ValidateException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage())
                .isEqualTo("Item request description can't be empty");
    }
}

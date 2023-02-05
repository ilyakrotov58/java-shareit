package ru.practicum.shareit.unitTests.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.EntityGenerator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserByIdTests {

    private IUserRepository userRepository;
    private IUserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);

        userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    }

    @Test
    void getUserById_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        var actualUser = userService.getById(user.getId());

        // Assert
        assertThat(actualUser)
                .hasFieldOrPropertyWithValue("id", userDto.getId())
                .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("lastName", userDto.getLastName());
    }

    @Test
    void getUserById_WhenUserIsNotExist_ShouldThrowException() {

        // Arrange
        RuntimeException exception = null;

        // Act
        try {
            userService.getById(100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }
}

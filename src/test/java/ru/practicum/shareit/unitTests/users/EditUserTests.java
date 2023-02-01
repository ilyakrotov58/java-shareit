package ru.practicum.shareit.unitTests.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.EntityGenerator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditUserTests {

    private IUserRepository userRepository;
    private IUserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);

        userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    }

    @Test
    void editUser_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var newUser = EntityGenerator.createUser();
        newUser.setId(1);

        var newUserDto = UserDtoMapper.toDto(newUser);

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(newUser);

        // Act
        var actualUser = userService.editById(newUserDto, user.getId());

        // Assert
        assertThat(actualUser)
                .hasFieldOrPropertyWithValue("id", newUserDto.getId())
                .hasFieldOrPropertyWithValue("email", newUserDto.getEmail())
                .hasFieldOrPropertyWithValue("name", newUserDto.getName())
                .hasFieldOrPropertyWithValue("lastName", newUserDto.getLastName());
    }

    @Test
    void editUser_WhenUserIsNotExist_ShouldThrowException() {

        // Arrange
        var newUser = EntityGenerator.createUser();
        var newUserDto = UserDtoMapper.toDto(newUser);

        RuntimeException exception = null;

        // Act
        try {
            userService.editById(newUserDto, 100);
        } catch (NotFoundException ex) {
            exception = ex;
        }

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNotNull();

        assertThat(exception.getMessage()).isEqualTo("User with id = 100 can't be found");
    }
}

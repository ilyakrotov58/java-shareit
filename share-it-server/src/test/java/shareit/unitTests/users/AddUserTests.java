package shareit.unitTests.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import shareit.user.dto.UserDtoMapper;
import shareit.user.model.User;
import shareit.user.repository.IUserRepository;
import shareit.user.service.IUserService;
import shareit.user.service.UserService;
import shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddUserTests {

    private IUserRepository userRepository;
    private IUserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);

        userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    }

    @Test
    void addUser_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        // Act
        var actualUser = userService.add(userDto);

        // Assert
        assertThat(actualUser)
                .hasFieldOrPropertyWithValue("id", userDto.getId())
                .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("lastName", userDto.getLastName());
    }
}

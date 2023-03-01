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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetAllUsersTests {

    private IUserRepository userRepository;
    private IUserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(IUserRepository.class);

        userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
    }

    @Test
    void getAllUsers_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        var userList = new ArrayList<User>();
        userList.add(user);

        Mockito
                .when(userRepository.findAll())
                .thenReturn(userList);

        // Act
        var actualUserList = userService.getAll();

        // Assert
        assertThat(actualUserList).hasSize(1);

        assertThat(actualUserList.get(0))
                .hasFieldOrPropertyWithValue("id", userDto.getId())
                .hasFieldOrPropertyWithValue("email", userDto.getEmail())
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("lastName", userDto.getLastName());
    }
}

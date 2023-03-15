package shareit.integrationTests.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.ShareItServerApp;
import shareit.booking.repository.IBookingRepository;
import shareit.item.repository.ICommentRepository;
import shareit.item.repository.IItemRepository;
import shareit.user.dto.UserDtoMapper;
import shareit.user.repository.IUserRepository;
import shareit.user.service.IUserService;
import shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItServerApp.class)
public class UsersIntTests {

    @Autowired
    IBookingRepository bookingRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ICommentRepository commentRepository;

    @Autowired
    IItemRepository itemRepository;

    @Autowired
    IUserService userService;

    @BeforeEach
    void clearDbBeforeTest() {
        commentRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void editUser_WithCorrectParams_ReturnsCorrectResponse() {

        // Arrange
        var user = EntityGenerator.createUser();
        var userInDb = userRepository.save(user);

        var newUser = EntityGenerator.createUser();
        var expectedUser = UserDtoMapper.toDto(newUser);
        expectedUser.setId(userInDb.getId());

        // Act
        var actualUser = userService.editById(expectedUser, userInDb.getId());

        // Assert
        assertThat(actualUser)
                .hasFieldOrPropertyWithValue("id", expectedUser.getId())
                .hasFieldOrPropertyWithValue("email", expectedUser.getEmail())
                .hasFieldOrPropertyWithValue("name", expectedUser.getName())
                .hasFieldOrPropertyWithValue("lastName", expectedUser.getLastName());
    }

    @AfterEach
    void clearDb() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        userRepository.deleteAll();
    }
}

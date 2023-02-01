package ru.practicum.shareit.integrationTests.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.repository.IBookingRepository;
import ru.practicum.shareit.item.repository.ICommentRepository;
import ru.practicum.shareit.item.repository.IItemRepository;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.repository.IUserRepository;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItApp.class)
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

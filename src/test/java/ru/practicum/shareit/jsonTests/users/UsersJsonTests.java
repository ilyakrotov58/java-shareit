package ru.practicum.shareit.jsonTests.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.utils.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UsersJsonTests {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testItemDto() throws Exception {

        // Arrange
        var userDto = UserDtoMapper.toDto(EntityGenerator.createUser());

        // Act
        JsonContent<UserDto> result = json.write(userDto);

        // Assert
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo((int) userDto.getId());

        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo(userDto.getEmail());

        assertThat(result).extractingJsonPathStringValue("$.name")
                .contains(userDto.getName());

        assertThat(result).extractingJsonPathStringValue("$.lastName")
                .isEqualTo(userDto.getLastName());
    }
}

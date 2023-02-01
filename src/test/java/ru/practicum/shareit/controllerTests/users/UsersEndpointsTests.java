package ru.practicum.shareit.controllerTests.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.EntityGenerator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UsersEndpointsTests {

    @Mock
    private UserService userService;

    @InjectMocks
    UserController controller;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllUsers_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);
        var listUserDto = new ArrayList<UserDto>();
        listUserDto.add(userDto);

        Mockito
                .when(userService.getAll())
                .thenReturn(listUserDto);

        // Act & Assert
        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(listUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is((int) userDto.getId())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].lastName", is(userDto.getLastName())));
    }

    @Test
    void getUserById_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        Mockito
                .when(userService.getById(Mockito.anyLong()))
                .thenReturn(userDto);

        // Act & Assert
        mvc.perform(get("/users/" + user.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userDto.getId())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())));
    }

    @Test
    void addUser_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        Mockito
                .when(userService.add(Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        // Act & Assert
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userDto.getId())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())));
    }

    @Test
    void editUser_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        Mockito
                .when(userService.editById(Mockito.any(UserDto.class), Mockito.anyLong()))
                .thenReturn(userDto);

        // Act & Assert
        mvc.perform(patch("/users/" + user.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userDto.getId())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())));
    }

    @Test
    void deleteUser_WithCorrectParams_ReturnsCorrectResponse() throws Exception {

        // Arrange
        var user = EntityGenerator.createUser();
        var userDto = UserDtoMapper.toDto(user);

        // Act & Assert
        mvc.perform(delete("/users/" + user.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

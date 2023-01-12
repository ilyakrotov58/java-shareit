package ru.practicum.shareit.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Tag(name = "Users")
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public UserDto getById(@PathVariable long userId) {
        return service.getById(userId);
    }

    @PostMapping
    @Operation(summary = "Add user")
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return service.add(userDto);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Edit user")
    public UserDto editById(@RequestBody UserDto userDto, @PathVariable long userId) {
        return service.editById(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user")
    public void deleteById(@PathVariable long userId) {
        service.deleteById(userId);
    }
}

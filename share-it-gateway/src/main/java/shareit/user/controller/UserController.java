package shareit.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.user.dto.UserDto;
import shareit.user.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/users")
@Tag(name = "Users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<Object> getAll() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        return service.getById(userId);
    }

    @PostMapping
    @Operation(summary = "Add user")
    public ResponseEntity<Object> add(@Valid @RequestBody UserDto userDto) {
        return service.add(userDto);
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Edit user")
    public ResponseEntity<Object> editById(@RequestBody UserDto userDto, @PathVariable long userId) {
        return service.editById(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Object> deleteById(@PathVariable long userId) {
        return service.deleteById(userId);
    }
}

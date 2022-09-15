package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface IUserService {

    List<UserDto> getAll();

    UserDto add(UserDto userDto);

    UserDto editById(UserDto userDto, long userId);

    UserDto getById(long userId);

    void deleteById(long userId);
}

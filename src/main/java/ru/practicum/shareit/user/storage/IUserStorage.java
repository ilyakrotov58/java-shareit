package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface IUserStorage {

    UserDto add(UserDto user);

    UserDto editById(UserDto user, long userId);

    List<UserDto> getAll();

    List<Long> getAllUserIds();

    UserDto getById(long userId);

    void deleteById(long userId);
}

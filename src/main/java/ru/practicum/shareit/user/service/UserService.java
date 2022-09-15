package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.IUserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserStorage userStorage;

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll();
    }

    @Override
    public UserDto add(UserDto userDto) {
        var returnedUser = userStorage.add(userDto);
        log.info(String.format("User with id=%s was added", returnedUser.getId()));

        return returnedUser;
    }

    @Override
    public UserDto editById(UserDto userDto, long userId) {
        var editedUser = userStorage.editById(userDto, userId);
        log.info(String.format("User with id=%s was edited", editedUser.getId()));

        return editedUser;
    }

    @Override
    public UserDto getById(long userId) {
        return userStorage.getById(userId);
    }

    @Override
    public void deleteById(long userId) {
        userStorage.deleteById(userId);
        log.info(String.format("User with id=%s was deleted", userId));
    }
}

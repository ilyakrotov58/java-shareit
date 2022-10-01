package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserStorage implements IUserStorage {

    private final Map<Long, User> inMemoryUserStorage = new HashMap<>();
    private long id = 0;

    @Override
    public UserDto add(UserDto userDto) {
        validateEmail(userDto);

        var user = UserDtoMapper.fromDto(userDto);
        user.setId(++id);
        userDto.setId(user.getId());

        inMemoryUserStorage.put(user.getId(), user);
        return userDto;
    }

    @Override
    public UserDto editById(UserDto userDto, long userId) {
        validateUserId(userId);
        validateEmail(userDto);

        var user = inMemoryUserStorage.get(userId);

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getReviews() != null) user.setReviews(userDto.getReviews());

        inMemoryUserStorage.replace(userId, user);

        return UserDtoMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return inMemoryUserStorage.entrySet().stream()
                .map(i -> UserDtoMapper.toDto(i.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllUserIds() {
        return inMemoryUserStorage.entrySet().stream()
                .map(i -> i.getValue().getId())
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long userId) {
        validateUserId(userId);
        var user = inMemoryUserStorage.get(userId);

        return UserDtoMapper.toDto(user);
    }

    @Override
    public void deleteById(long userId) {
        validateUserId(userId);

        inMemoryUserStorage.remove(userId);
    }

    private void validateUserId(long userId) {
        if (inMemoryUserStorage.get(userId) == null) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }
    }

    private void validateEmail(UserDto userDto) {
        for (User user : inMemoryUserStorage.values()) {
            if (user.getEmail().equals(userDto.getEmail())) {
                throw new ValidateException(String.format("User with email %s already exist", userDto.getEmail()));
            }
        }
    }
}

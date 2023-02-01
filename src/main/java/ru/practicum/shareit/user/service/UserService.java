package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.IUserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        var users = userRepository.findAll();
        var usersDto = new ArrayList<UserDto>();

        for (User user : users) {
            usersDto.add(UserDtoMapper.toDto(user));
        }

        return usersDto;
    }

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {

        var user = UserDtoMapper.fromDto(userDto);
        var returnedUser = userRepository.save(user);
        log.info(String.format("User with id=%s was added", returnedUser.getId()));

        return UserDtoMapper.toDto(returnedUser);
    }

    @Override
    @Transactional
    public UserDto editById(UserDto userDto, long userId) {

        checkIfUserExists(userId);

        userDto.setId(userId);
        var oldUser = userRepository.findById(userId).get();

        if (userDto.getEmail() == null) userDto.setEmail(oldUser.getEmail());
        if (userDto.getName() == null) userDto.setName(oldUser.getName());
        if (userDto.getLastName() == null) userDto.setLastName(oldUser.getLastName());

        userRepository.save(UserDtoMapper.fromDto(userDto));

        log.info(String.format("User with id=%s was edited", userDto.getId()));

        userDto.setId(userId);
        return userDto;
    }

    @Override
    public UserDto getById(long userId) {

        checkIfUserExists(userId);

        var user = userRepository.findById(userId);

        return UserDtoMapper.toDto(user.get());
    }

    @Override
    @Transactional
    public void deleteById(long userId) {

        checkIfUserExists(userId);

        userRepository.deleteById(userId);

        log.info(String.format("User with id=%s was deleted", userId));
    }

    private void checkIfUserExists(long userId) {
        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id = %s can't be found", userId));
        }
    }
}

package shareit.user.dto;

import shareit.user.model.User;

public class UserDtoMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getLastName());
    }

    public static User fromDto(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getName(),
                userDto.getLastName());
    }
}

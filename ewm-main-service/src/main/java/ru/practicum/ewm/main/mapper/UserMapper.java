package ru.practicum.ewm.main.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.UserShortDto;
import ru.practicum.ewm.main.entity.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto()
                .setEmail(user.getEmail())
                .setName(user.getName());
    }

    public static User toUser(UserDto userDto) {
        return new User()
                .setEmail(userDto.getEmail())
                .setName(userDto.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto()
                .setId(user.getId())
                .setName(user.getName());
    }
}

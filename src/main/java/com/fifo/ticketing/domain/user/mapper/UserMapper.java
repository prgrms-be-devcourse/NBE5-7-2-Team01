package com.fifo.ticketing.domain.user.mapper;

import com.fifo.ticketing.domain.user.dto.UserDto;
import com.fifo.ticketing.domain.user.entity.User;
import org.springframework.data.domain.Page;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .isBlocked(user.isBlocked())
            .role(user.getRole())
            .build();
    }

    public static Page<UserDto> toUserDtoPage(Page<User> users) {
        return users.map(UserMapper::toUserDto);
    }

}

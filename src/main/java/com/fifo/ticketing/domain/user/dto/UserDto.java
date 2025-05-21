package com.fifo.ticketing.domain.user.dto;

import com.fifo.ticketing.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private Boolean isBlocked;
    private Role role;

}

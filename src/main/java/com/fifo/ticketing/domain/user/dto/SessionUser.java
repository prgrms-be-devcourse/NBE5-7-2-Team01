package com.fifo.ticketing.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record SessionUser(@NotEmpty(message = "id는 필수입니다.") Long id,
                          @NotEmpty(message = "유저명은 필수입니다.") String username) {

}

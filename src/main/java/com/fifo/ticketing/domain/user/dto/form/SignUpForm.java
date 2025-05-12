package com.fifo.ticketing.domain.user.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignUpForm(@Email @NotEmpty(message = "이메일은 필수 입니다.") String email,
                         @NotEmpty(message = "유저명은 필수 입니다.") String username,
                         @NotEmpty(message = "비밀번호는 필수 입니다.") String password) {

}

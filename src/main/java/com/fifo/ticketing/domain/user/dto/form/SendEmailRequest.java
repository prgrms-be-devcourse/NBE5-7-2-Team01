package com.fifo.ticketing.domain.user.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SendEmailRequest(@Email @NotEmpty(message = "이메일 입력해주세요") String email) {

}

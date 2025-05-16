package com.fifo.ticketing.global.util;

import com.fifo.ticketing.domain.user.dto.SessionUser;
import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import jakarta.servlet.http.HttpSession;

public class UserValidator {

    private UserValidator() {
    }

    public static Long validateSessionUser(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("loginUser");
        if (user == null) {
            throw new ErrorException("user/sign_in", ErrorCode.UNAUTHORIZED_REQUEST);
        }

        return user.id();
    }
}

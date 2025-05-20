package com.fifo.ticketing.global.util;

import com.fifo.ticketing.global.exception.ErrorCode;
import com.fifo.ticketing.global.exception.ErrorException;
import java.time.LocalDateTime;

public class DateTimeValidator {

    private DateTimeValidator() {

    }

    public static void periodValidator(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new ErrorException(ErrorCode.INVALID_DATETIME_TYPE);
        }
        if (startDate.isAfter(endDate)) {
            throw new ErrorException(ErrorCode.INVALID_DATETIME_PERIOD);
        }
    }
}

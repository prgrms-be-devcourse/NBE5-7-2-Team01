package com.fifo.ticketing.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({ErrorException.class, AlertDetailException.class})
    public Object handleException(
        RuntimeException ex,
        Model model,
        HandlerMethod handlerMethod
    ) {
        boolean isApiRequest = AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(),
            ResponseBody.class);

        // 예외 타입에 따라 처리
        if (ex instanceof ErrorException errorException) {
            return handleCommonException(
                errorException.getErrorCode(),
                errorException.getUrl(),
                isApiRequest,
                model,
                errorException
            );
        } else if (ex instanceof AlertDetailException alertDetailException) {
            return handleCommonException(
                alertDetailException.getErrorCode(),
                alertDetailException.getUrl(),
                isApiRequest,
                model,
                alertDetailException
            );
        }
        // 알 수 없는 예외에 대해서는 기본 에러 처리
        log.error("Unhandled exception: ", ex);
        if (isApiRequest) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("500", "Unexpected error occurred"));
        } else {
            model.addAttribute("message", "Unexpected error occurred");
            model.addAttribute("url", "/");
            return "error/alert";
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlePageNotFoundException(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "404 Page not found");
        return "error/404";
    }

    private Object handleCommonException(
        ErrorCode errorCode,
        String url,
        boolean isApiRequest,
        Model model,
        Exception ex
    ) {
        log.error(errorCode.getMessage(), ex);

        if (isApiRequest) {
            HttpStatus httpStatus = switch (errorCode.getErrorStatus()) {
                case NOT_FOUND -> HttpStatus.NOT_FOUND;
                // 필요한 경우 다른 상태 추가
                case CONFLICT -> HttpStatus.CONFLICT;
                case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
                case ALREADY_EXISTS, BAD_REQUEST -> HttpStatus.BAD_REQUEST;
                case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            };

            return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
        } else {
            model.addAttribute("message", errorCode.getMessage());
            model.addAttribute("url", url);
            return "error/alert";
        }
    }
}

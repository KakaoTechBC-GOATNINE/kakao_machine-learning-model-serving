package com.example.kakao_mlms.exception;

import com.example.kakao_mlms.dto.ArgumentNotValidExceptionDto;
import com.example.kakao_mlms.dto.ExceptionDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.FileNotFoundException;

@Builder
public record ResponseDto<T>(@JsonIgnore HttpStatus httpStatus,
                             @NotNull Boolean success,
                             @Nullable T data,
                             @Nullable ExceptionDto error) {

    public static <T> ResponseDto<T> ok(@Nullable T data) { //성공한 경우
        return new ResponseDto<T>(HttpStatus.OK, true, data, null);
    }

    public static <T> ResponseDto<T> created(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.CREATED, true, data, null);
    }

    public static ResponseDto<Object> fail(final HandlerMethodValidationException e) { //실패한 경우
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(ErrorCode.INVALID_PARAMETER));
    }

    public static ResponseDto<Object> fail(final CommonException e) { //실패한 경우
        return new ResponseDto<>(e.getErrorCode().getHttpStatus(), false, null, new ExceptionDto(e.getErrorCode()));
    }

    public static ResponseDto<Object> fail(final MethodArgumentNotValidException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ArgumentNotValidExceptionDto(e));
    }

    public static ResponseDto<Object> fail(final MethodArgumentTypeMismatchException e) {
        return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, false, null, new ExceptionDto(ErrorCode.INVALID_PARAMETER_FORMAT));
    }

    public static ResponseDto<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, false, null, new ExceptionDto(ErrorCode.MISSING_REQUEST_PARAMETER));
    }

    public static ResponseDto<Object> fail(FileNotFoundException e) {
        return new ResponseDto<>(HttpStatus.NOT_FOUND, false, null, new ExceptionDto(ErrorCode.NOT_FOUND_FILE));
    }
}

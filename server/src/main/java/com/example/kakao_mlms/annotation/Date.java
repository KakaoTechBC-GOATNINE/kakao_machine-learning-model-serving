package com.example.kakao_mlms.annotation;

import com.example.kakao_mlms.validator.LocalDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Date {
    //유효성 검사 실패 시 반환될 메시지
    String message() default "Invalid Date Format. (yyyy-MM-dd)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

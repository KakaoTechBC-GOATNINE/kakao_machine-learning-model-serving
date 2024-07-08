package com.example.kakao_mlms.validator;

import com.example.kakao_mlms.annotation.Date;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class LocalDateValidator implements ConstraintValidator<Date, String> {
    //static 사용
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public void initialize(Date constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //null check
        if (value == null || value.length() == 0) {
            return false;
        }

        try {
            LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }
}

package ru.itis.tdagameanalytics.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.itis.tdagameanalytics.validator.PasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Пароль должен содержать минимум 8 символов, цифры, буквы в верхнем и нижнем регистре";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

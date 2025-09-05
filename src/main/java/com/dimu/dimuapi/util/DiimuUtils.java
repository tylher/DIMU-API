package com.dimu.dimuapi.util;

import jakarta.validation.*;

import java.util.Set;

public class DiimuUtils {
    public static  <T> void validateInput(T input) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}

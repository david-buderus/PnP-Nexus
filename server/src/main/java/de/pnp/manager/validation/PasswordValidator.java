package de.pnp.manager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for passwords.
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final int MIN_LENGTH = 16;

    private static final int MAX_LENGTH = 64;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() >= MIN_LENGTH && value.length() < MAX_LENGTH && !value.isBlank()
            && value.chars()
            .anyMatch(Character::isDigit) && value.chars().anyMatch(Character::isUpperCase) && value.chars()
            .anyMatch(Character::isLowerCase) && value.chars().anyMatch(PasswordValidator::isSpecialCharacter);
    }

    private static boolean isSpecialCharacter(int c) {
        return !Character.isLetter(c) && !Character.isDigit(c);
    }
}

package de.pnp.manager.validation;

import de.pnp.manager.server.database.UserDetailsRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Validator for current passwords.
 */
public class ValidCurrentPasswordValidator implements ConstraintValidator<ValidCurrentPassword, String> {

    @Autowired
    private UserDetailsRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.isValidPassword(username, value);
    }
}

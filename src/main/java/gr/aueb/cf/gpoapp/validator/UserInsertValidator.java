package gr.aueb.cf.gpoapp.validator;

import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Custom Validator για το UserInsertDTO.
 * Ελέγχει τη μοναδικότητα των στοιχείων στη βάση δεδομένων (Business Validation).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserInsertValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserInsertDTO.class == clazz;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserInsertDTO userInsertDTO = (UserInsertDTO) target;

        // Έλεγχος μοναδικότητας Username
        if (userRepository.findByUsername(userInsertDTO.getUsername()).isPresent()) {
            log.warn("Validation failed: Username '{}' already exists", userInsertDTO.getUsername());
            // rejectValue(field, errorCode, defaultMessage)
            errors.rejectValue("username", "username.exists", "Το όνομα χρήστη χρησιμοποιείται ήδη.");
        }

        // Έλεγχος μοναδικότητας Email
        if (userRepository.findByEmail(userInsertDTO.getEmail()).isPresent()) {
            log.warn("Validation failed: Email '{}' already exists", userInsertDTO.getEmail());
            errors.rejectValue("email", "email.exists", "Η διεύθυνση email είναι ήδη εγγεγραμμένη.");
        }

        // Έλεγχος μοναδικότητας VAT
        if (userRepository.findByVat(userInsertDTO.getVat()).isPresent()) {
            log.warn("Validation failed: VAT '{}' already exists", userInsertDTO.getVat());
            errors.rejectValue("vat", "vat.exists", "Το ΑΦΜ χρησιμοποιείται ήδη.");
        }
    }
}
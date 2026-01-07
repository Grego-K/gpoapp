package gr.aueb.cf.gpoapp.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInsertDTO {

    @NotBlank(message = "Το email είναι υποχρεωτικό")
    @Email(message = "Παρακαλώ εισάγετε ένα έγκυρο email")
    private String email;

    @NotBlank(message = "Το username είναι υποχρεωτικό")
    @Size(min = 3, max = 20, message = "Το username πρέπει να είναι 3-20 χαρακτήρες")
    private String username;

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    private String firstname;

    @NotBlank(message = "Το επώνυμο είναι υποχρεωτικό")
    private String lastname;

    @NotBlank(message = "Ο κωδικός είναι υποχρεωτικός")
    @Size(min = 8, message = "Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες")
    private String password;

    @Pattern(regexp = "^$|[0-9]{10}", message = "Το τηλέφωνο πρέπει να είναι 10 ψηφία")
    private String phonenumber;

    @NotBlank(message = "Το ΑΦΜ είναι υποχρεωτικό")
    @Pattern(regexp = "\\d{9}", message = "Το ΑΦΜ πρέπει να έχει 9 ψηφία")
    private String vat;

    @NotNull(message = "Παρακαλώ επιλέξτε περιοχή")
    private Long region;
}
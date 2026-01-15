package gr.aueb.cf.gpoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    @Size(min = 3, max = 50, message = "Το όνομα πρέπει να είναι 3-50 χαρακτήρες")
    private String name;

    private String description;
}
package gr.aueb.cf.gpoapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Το όνομα προϊόντος είναι υποχρεωτικό")
    private String productName;

    private String description;

    @NotNull(message = "Η αρχική τιμή είναι υποχρεωτική")
    @DecimalMin(value = "0.0", inclusive = false, message = "Η τιμή πρέπει να είναι θετική")
    private BigDecimal basePrice;

    @NotNull(message = "Η τιμή GPO είναι υποχρεωτική")
    @DecimalMin(value = "0.0", inclusive = false, message = "Η τιμή GPO πρέπει να είναι θετική")
    private BigDecimal gpoPrice;

    @NotNull(message = "Το απόθεμα είναι υποχρεωτικό")
    private Integer stockQuantity;

    @NotNull(message = "Πρέπει να επιλέξετε κατηγορία")
    private Long categoryId;

    @NotNull(message = "Πρέπει να επιλέξετε προμηθευτή")
    private Long supplierId;
}
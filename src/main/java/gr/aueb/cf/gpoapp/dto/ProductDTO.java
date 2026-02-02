package gr.aueb.cf.gpoapp.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {

    private Long id;
    private String uuid;

    @NotBlank(message = "Το όνομα του προϊόντος είναι υποχρεωτικό")
    private String productName;

    private String description;

    @NotNull(message = "Η αρχική τιμή είναι υποχρεωτική")
    @DecimalMin(value = "0.01", message = "Η τιμή πρέπει να είναι θετική")
    private BigDecimal basePrice;

    @NotNull(message = "Η τιμή GPO είναι υποχρεωτική")
    @DecimalMin(value = "0.01", message = "Η τιμή GPO πρέπει να είναι θετική")
    private BigDecimal gpoPrice;

    @NotNull(message = "Το απόθεμα είναι υποχρεωτικό")
    @Min(value = 0, message = "Το απόθεμα δεν μπορεί να είναι αρνητικό")
    private Integer stockQuantity;

    // Προσθήκη των IDs (Απαραίτητα για τον Controller/Service)
    @NotNull(message = "Η επιλογή κατηγορίας είναι υποχρεωτική")
    private Long categoryId;

    @NotNull(message = "Η επιλογή προμηθευτή είναι υποχρεωτική")
    private Long supplierId;

    // Προσθήκη των Names (Απαραίτητα για το UI)
    private String categoryName;
    private String supplierName;

    // --- Rebate & Progress Fields ---
    private Integer currentVolume;
    private Integer nextTierThreshold;
    private Integer progressPercent;
    private String currentRebateLabel;
    private String nextRebateLabel;

    // Αρχικοποίηση λίστας για να δέχεται τα δυναμικά πεδία της φόρμας
    @Builder.Default
    private List<RebateTierDTO> rebateTiers = new ArrayList<>();
}
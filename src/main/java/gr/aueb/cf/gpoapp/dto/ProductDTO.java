package gr.aueb.cf.gpoapp.dto;

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
    private String productName;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal gpoPrice;
    private Integer stockQuantity;

    // Προσθήκη των IDs (Απαραίτητα για τον Controller/Service)
    private Long categoryId;
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
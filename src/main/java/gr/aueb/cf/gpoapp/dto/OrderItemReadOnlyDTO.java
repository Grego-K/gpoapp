package gr.aueb.cf.gpoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// Read-Only DTO για τα στοιχεία (items) μιας παραγγελίας
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemReadOnlyDTO {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;         // Η τιμή GPO
    private BigDecimal estimatedItemRebate; // Το συνολικό rebate για τη γραμμή
    private BigDecimal netTierPrice;      //  Η καθαρή τιμή ανά τεμάχιο μετά το rebate
}
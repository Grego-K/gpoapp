package gr.aueb.cf.gpoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReadOnlyDTO {
    private Long id;
    private String uuid;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt; // Αλλαγή σε LocalDateTime για να ταιριάζει με το Entity
    private List<String> supplierNames;
    private int itemCount;
    private List<OrderItemReadOnlyDTO> items;
}
package gr.aueb.cf.gpoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// Read-only DTO που αναπαριστά μια παραγγελία όπως επιστρέφεται στο API.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReadOnlyDTO {
    private String uuid;
    private String status;
    private BigDecimal totalAmount;
    private String createdAt;
    private List<OrderItemReadOnlyDTO> items;
}
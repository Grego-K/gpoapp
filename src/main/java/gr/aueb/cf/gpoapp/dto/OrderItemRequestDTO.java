package gr.aueb.cf.gpoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO για την παραλαβή δεδομένων με χρήση "μαζικής παραγγελίας" από το Frontend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {
    private Long productId;
    private Integer quantity;
}
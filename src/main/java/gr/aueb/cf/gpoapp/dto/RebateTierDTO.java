package gr.aueb.cf.gpoapp.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebateTierDTO {
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal rebateAmount;
    private String periodType;
}
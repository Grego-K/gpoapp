package gr.aueb.cf.gpoapp.model;

import gr.aueb.cf.gpoapp.model.enums.PeriodType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "rebate_tiers",
        indexes = {
                @Index(name = "idx_rebate_product_period", columnList = "product_id, periodType")
        }
)
public class RebateTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer minQuantity;

    @Column(nullable = false)
    private Integer maxQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rebateAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType = PeriodType.QUARTER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
// Φέυγει απο εδώ --> Enums/PeriodType
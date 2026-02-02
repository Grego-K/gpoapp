package gr.aueb.cf.gpoapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "rebate_tiers")
public class RebateTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer minQuantity;

    @Column(nullable = false)
    private Integer maxQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rebateAmount; // Το ποσό που επιστρέφεται ανά τεμάχιο

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType = PeriodType.QUARTER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}

enum PeriodType {
    MONTH, QUARTER, YEAR
}
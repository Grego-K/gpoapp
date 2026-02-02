package gr.aueb.cf.gpoapp.model;

import gr.aueb.cf.gpoapp.model.enums.PeriodType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "product_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "periodLabel"})
        }
)
public class ProductProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer volume = 0;

    @Column(nullable = false)
    private String periodLabel; // π.χ. "2026_Q1", "2026_Q2" κλπ

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;
}
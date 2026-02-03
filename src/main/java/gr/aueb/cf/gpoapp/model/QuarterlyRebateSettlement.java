package gr.aueb.cf.gpoapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rebate_settlements")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class QuarterlyRebateSettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String periodLabel; // π.χ. "2026_Q1"
    private BigDecimal totalRebateAmount;

    private LocalDateTime settledAt;

    @Enumerated(EnumType.STRING)
    private SettlementStatus status; // PENDING, PAID

    public enum SettlementStatus { PENDING, PAID }
}
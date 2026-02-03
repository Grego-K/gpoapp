package gr.aueb.cf.gpoapp.core.filters;

import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Αφαιρέσαμε το @Builder για fix του form binding

public class OrderFilters {

    @Nullable
    private OrderStatus status;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @Nullable
    private Long supplierId;

    private Pageable pageable;
}
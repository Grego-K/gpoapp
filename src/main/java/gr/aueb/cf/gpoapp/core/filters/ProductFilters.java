package gr.aueb.cf.gpoapp.core.filters;

import lombok.*;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductFilters extends GenericFilters {

    @Nullable
    private String productName;

    @Nullable
    private Long categoryId;

    //TBD supplier filtering
}
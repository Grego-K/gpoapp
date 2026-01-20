package gr.aueb.cf.gpoapp.core.filters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
@Builder
public class Paginated<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private int numberOfElements;
    private long totalElements;

    public static <T> Paginated<T> fromPage(Page<T> page) {
        return Paginated.<T>builder()
                .data(page.getContent())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .build();
    }
}
package gr.aueb.cf.gpoapp.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductReadOnlyDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String categoryName;
}
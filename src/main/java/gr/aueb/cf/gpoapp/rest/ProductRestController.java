package gr.aueb.cf.gpoapp.rest;

import gr.aueb.cf.gpoapp.dto.ProductReadOnlyDTO;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final IProductService productService;

    // Επιστρέφει τη λίστα όλων των προϊόντων.
    @GetMapping
    public ResponseEntity<List<ProductReadOnlyDTO>> getAllProducts() {
        try {
            List<Product> products = productService.findAllProducts();

            List<ProductReadOnlyDTO> dtos = products.stream()
                    .map(this::mapToReadOnlyDTO)
                    .toList();

            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Επιστρέφει ένα συγκεκριμένο προϊόν βάσει του ID του.
    @GetMapping("/{id}")
    public ResponseEntity<ProductReadOnlyDTO> getProductById(@PathVariable Long id) {
        try {
            // Η findProductById χρησιμοποιεί πλέον findByIdWithRelations στο Repository
            Product product = productService.findProductById(id);
            ProductReadOnlyDTO dto = mapToReadOnlyDTO(product);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            // Επιστροφή 404 αν το προϊόν δεν βρεθεί
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Helper method για τη μετατροπή Entity σε DTO
     */
    private ProductReadOnlyDTO mapToReadOnlyDTO(Product product) {
        return ProductReadOnlyDTO.builder()
                .id(product.getId())
                .name(product.getProductName())
                .description(product.getDescription())
                .price(product.getBasePrice() != null ? product.getBasePrice().doubleValue() : 0.0)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : "N/A")
                .build();
    }
}
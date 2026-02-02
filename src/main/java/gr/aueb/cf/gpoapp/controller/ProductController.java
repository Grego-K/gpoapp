package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.service.ICategoryService;
import gr.aueb.cf.gpoapp.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final ICategoryService categoryService;

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute("filters") ProductFilters filters,
            Model model) {

        // Ενημερώνουμε τα πεδία φίλτρων για τη σελιδοποίηση
        filters.setPage(page);
        filters.setPageSize(size);

        /* * Καλούμε τη νέα μέθοδο που επιστρέφει Page<ProductDTO>.
         * Αυτό διασφαλίζει ότι κάθε αντικείμενο στη λίστα έχει ήδη
         * υπολογισμένο το volume και το progress percentage.
         */
        Page<ProductDTO> productPage = productService.getFilteredProductsDTO(filters);

        /* * Στέλνουμε το Page<ProductDTO> στο μοντέλο.
         * Το Thymeleaf θα μπορεί να διαβάσει τα νέα πεδία (π.χ. progressPercent).
         */
        model.addAttribute("products", productPage);
        model.addAttribute("categories", categoryService.findAllCategories());

        // Κρατάμε τα filters για να μην χάνονται οι επιλογές του χρήστη στο UI
        model.addAttribute("filters", filters);

        return "products/list";
    }
}
package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.model.Product;
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

        // Λήψη του Page αντικειμένου από το service
        Page<Product> productPage = productService.getFilteredProducts(filters);

        /* * Στέλνουμε το productPage ως "products".
         * Έτσι το Thymeleaf template μπορεί να καλέσει:
         * 1. products.content (για τη λίστα)
         * 2. products.totalPages (για τα κουμπιά pagination)
         */
        model.addAttribute("products", productPage);
        model.addAttribute("categories", categoryService.findAllCategories());

        // Κρατάμε τα filters στο model για να παραμένουν οι τιμές στα inputs της φόρμας
        model.addAttribute("filters", filters);

        return "products/list";
    }
}
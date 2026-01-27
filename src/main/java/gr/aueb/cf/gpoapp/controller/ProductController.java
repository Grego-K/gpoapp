package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.core.filters.Paginated;
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

        // Ενημερώνουμε τα πεδία που κληρονομούνται από τη GenericFilters
        filters.setPage(page);
        filters.setPageSize(size);

        Page<Product> productPage = productService.getFilteredProducts(filters);
        Paginated<Product> paginated = Paginated.fromPage(productPage);

        model.addAttribute("products", paginated.getData());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("pagination", paginated);

        return "products/list";
    }
}
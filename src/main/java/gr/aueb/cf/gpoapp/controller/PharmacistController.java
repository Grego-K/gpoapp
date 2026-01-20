package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.core.filters.Paginated;
import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.service.ICategoryService;
import gr.aueb.cf.gpoapp.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pharmacist")
@RequiredArgsConstructor
public class PharmacistController {

    private final IProductService productService;
    private final ICategoryService categoryService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "pharmacist/dashboard";
    }

    /**
     * Εμφανίζει τη λίστα προϊόντων με υποστήριξη φιλτραρίσματος και σελιδοποίησης.
     * Το ProductFilters γεμίζει αυτόματα από τις παραμέτρους του request.
     */
    @GetMapping("/products")
    @Transactional(readOnly = true)
    public String showProducts(@ModelAttribute("filters") ProductFilters filters, Model model) {

        // Ανάκτηση δεδομένων από το Service
        Page<Product> productPage = productService.getFilteredProducts(filters);

        // Μετατροπή σε paginated DTO
        Paginated<Product> paginated = Paginated.fromPage(productPage);

        // Προσθήκη δεδομένων στο μοντέλο για το Thymeleaf
        model.addAttribute("products", paginated.getData());
        model.addAttribute("pagination", paginated);

        // Χρειαζόμαστε όλες τις κατηγορίες για το drop-down φίλτρο
        model.addAttribute("categories", categoryService.findAllCategories());

        return "pharmacist/products";
    }
}
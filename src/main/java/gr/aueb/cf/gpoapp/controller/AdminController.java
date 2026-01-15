package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.CategoryDTO;
import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.service.ICategoryService;
import gr.aueb.cf.gpoapp.service.IProductService;
import gr.aueb.cf.gpoapp.service.ISupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ICategoryService categoryService;
    private final IProductService productService;
    private final ISupplierService supplierService;

    // Εμφάνιση του κεντρικού Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    // Εμφάνιση της φόρμας για την προσθήκη νέας κατηγορίας
    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "admin/add-category";
    }

    // Επεξεργασία της φόρμας και αποθήκευση νέας κατηγορίας
    @PostMapping("/categories/save")
    public String saveCategory(@Valid @ModelAttribute("categoryDTO") CategoryDTO categoryDTO,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/add-category";
        }

        try {
            categoryService.saveCategory(categoryDTO);
            model.addAttribute("successMessage", "Η κατηγορία '" + categoryDTO.getName() + "' δημιουργήθηκε επιτυχώς!");
            model.addAttribute("categoryDTO", new CategoryDTO()); // Καθαρισμός φόρμας
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Σφάλμα κατά την αποθήκευση: " + e.getMessage());
        }

        return "admin/add-category";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());

        // Φορτώνουμε τις λίστες dropdown
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("suppliers", supplierService.findAllSuppliers());

        return "admin/add-product";
    }

    // Αποθήκευση του νέου προϊόντος
    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Σε περίπτωση σφάλματος, ξαναγεμίζουμε τις λίστες για τα dropdowns
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("suppliers", supplierService.findAllSuppliers());
            return "admin/add-product";
        }

        try {
            productService.saveProduct(productDTO);
            // Προσθήκη μηνύματος επιτυχίας και καθαρισμός της φόρμας
            model.addAttribute("successMessage", "Το προϊόν '" + productDTO.getProductName() + "' αποθηκεύτηκε επιτυχώς!");
            model.addAttribute("productDTO", new ProductDTO());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Σφάλμα κατά την αποθήκευση του προϊόντος: " + e.getMessage());
        }

        // Φορτώνουμε ξανά τις λίστες dropdown για να είναι διαθέσιμες στη σελίδα
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("suppliers", supplierService.findAllSuppliers());

        return "admin/add-product";
    }
}
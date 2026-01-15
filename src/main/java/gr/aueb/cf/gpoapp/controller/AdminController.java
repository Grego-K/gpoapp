package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.CategoryDTO;
import gr.aueb.cf.gpoapp.service.ICategoryService;
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

    // Εμφάνιση του κεντρικού Admin Dashboard.
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    //Επεξεργασία της φόρμας και αποθήκευση νέας κατηγορίας.
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

    /**
     * Placeholder για προσθήκη προϊόντος μελλοντικά
     */
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        // Θα επιστρέφει τη φόρμα προϊόντος
        return "admin/add-product";
    }
}
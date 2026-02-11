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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ICategoryService categoryService;
    private final IProductService productService;
    private final ISupplierService supplierService;

    // Εμφάνιση του admin dashboard
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    // --- CATEGORIES SECTION ---
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

    // --- PRODUCTS SECTION ---

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

        // Έλεγχος Validation (σταματάμε τα NULL πριν πάνε στη βάση)
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

    /**
     * Εμφάνιση της φόρμας επεξεργασίας.
     * Χρησιμοποιούμε τη findProductDTOById που έχουμε στο Service για να φέρουμε έτοιμο DTO.
     */
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        try {
            ProductDTO productDTO = productService.findProductDTOById(id);
            model.addAttribute("productDTO", productDTO);
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("suppliers", supplierService.findAllSuppliers());
            return "admin/edit-product";
        } catch (Exception e) {
            return "redirect:/products?errorNotFound";
        }
    }

    /**
     * Επεξεργασία και αποθήκευση αλλαγών με Validation.
     */
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        // Έλεγχος για Validation Errors (DTO annotations + Custom Business Logic)
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("suppliers", supplierService.findAllSuppliers());
            return "admin/edit-product";
        }

        try {
            productService.updateProduct(id, productDTO);
            // Χρήση RedirectAttributes για να "αντέξει" το μήνυμα μετά το redirect
            redirectAttributes.addFlashAttribute("successUpdateMessage", "Το προϊόν ενημερώθηκε επιτυχώς!");
            return "redirect:/products?successUpdate";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Σφάλμα κατά την ενημέρωση: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("suppliers", supplierService.findAllSuppliers());
            return "admin/edit-product";
        }
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successDeleteMessage", "Το προϊόν διαγράφηκε.");
            return "redirect:/products?successDelete";
        } catch (Exception e) {
            return "redirect:/products?errorDelete";
        }
    }
}
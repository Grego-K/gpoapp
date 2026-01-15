package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.CategoryDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "admin/category";
    }
}
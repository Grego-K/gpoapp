package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.repository.RegionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserInsertDTO());
        model.addAttribute("regions", regionRepository.findAllByOrderByNameAsc());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserInsertDTO dto,
                               BindingResult bindingResult,
                               Model model) {

        // Έλεγχος Validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("regions", regionRepository.findAllByOrderByNameAsc());
            return "register";
        }

        // Logs
        System.out.println("Επιτυχές Validation για: " + dto.getEmail());

        return "redirect:/login";
    }
}
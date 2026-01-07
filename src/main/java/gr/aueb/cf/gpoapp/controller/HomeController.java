package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.model.static_data.Region;
import gr.aueb.cf.gpoapp.repository.RegionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RegionRepository regionRepository;

    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDTO", new UserInsertDTO());
        model.addAttribute("regions", regionRepository.findAllByOrderByNameAsc());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserInsertDTO dto,
                               BindingResult bindingResult,
                               Model model) {

        // Έλεγχος αν υπάρχουν σφάλματα
        if (bindingResult.hasErrors()) {
            model.addAttribute("regions", regionRepository.findAllByOrderByNameAsc());
            // Επιστροφή στην ίδια σελίδα για εμφάνιση των errors
            return "register";
        }

        // Προσωρινό Log για επιβεβαίωση ότι το DTO γέμισε
        System.out.println("Επιτυχές Validation για: " + dto.getEmail());
        System.out.println("Επιλεγμένο Region ID: " + dto.getRegion());

        // Ανακατεύθυνση στο login μετά την ολοκλήρωση συμπλήρωσης
        return "redirect:/login";
    }
}
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
        // Συνδέουμε το "userDTO" της HTML με ένα νέο UserInsertDTO αντικείμενο
        model.addAttribute("userDTO", new UserInsertDTO());
        // Γεμίζουμε τη λίστα των περιοχών από τη βάση
        model.addAttribute("regions", regionRepository.findAllByOrderByNameAsc());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserInsertDTO userInsertDTO,
                               BindingResult bindingResult,
                               Model model) {

        // Έλεγχος για Validation Errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("regions", regionRepository.findAllByOrderByNameAsc());
            return "register";
        }

        // Logging για επιβεβαίωση
        System.out.println("Προσπάθεια εγγραφής: " + userInsertDTO.getEmail());
        System.out.println("Username: " + userInsertDTO.getUsername());
        System.out.println("Region ID: " + userInsertDTO.getRegion());

        // Ανακατεύθυνση στο login
        return "redirect:/login";
    }
}
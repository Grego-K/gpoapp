package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.model.static_data.Region;
import gr.aueb.cf.gpoapp.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private RegionRepository regionRepository; // Κάνουμε inject το repository για να έχουμε πρόσβαση στη MySQL

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
        // 1. Στέλνουμε ένα άδειο DTO για το form binding
        model.addAttribute("userDTO", new UserInsertDTO());

        // 2. Τραβάμε τις περιοχές από τη βάση ταξινομημένες αλφαβητικά
        List<Region> regions = regionRepository.findAllByOrderByNameAsc();

        // 3. Τις προσθέτουμε στο μοντέλο για να τις δει το Thymeleaf
        model.addAttribute("regions", regions);

        return "register";
    }
}
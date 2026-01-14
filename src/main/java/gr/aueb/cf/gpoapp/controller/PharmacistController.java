package gr.aueb.cf.gpoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pharmacist")
public class PharmacistController {

    @GetMapping("/dashboard")
    public String dashboard() {
        // θα ψάξει το αρχείο στο: src/main/resources/templates/pharmacist/dashboard.html
        return "pharmacist/dashboard";
    }
}
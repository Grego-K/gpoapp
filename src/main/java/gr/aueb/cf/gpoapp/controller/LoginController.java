package gr.aueb.cf.gpoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class LoginController {

    // Διαχειρίζεται το URL /login
    @GetMapping("/login")
    public String login(Principal principal) {
        // Αν είναι συνδεδεμένος, τον στέλνουμε στο dashboard, αλλιώς στη φόρμα login
        return principal == null ? "login" : "redirect:/pharmacist/dashboard";
    }

    // Διαχειρίζεται το URL / (την αρχική σελίδα)
    @GetMapping("/")
    public String root(Principal principal) {
        // Αν δεν είναι συνδεδεμένος, δείξε το landing.html, αλλιώς το dashboard
        return principal == null ? "landing" : "redirect:/pharmacist/dashboard";
    }
}
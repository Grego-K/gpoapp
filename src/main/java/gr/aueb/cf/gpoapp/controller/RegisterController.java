package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.gpoapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.model.static_data.Region;
import gr.aueb.cf.gpoapp.repository.RegionRepository;
import gr.aueb.cf.gpoapp.service.IUserService;
import gr.aueb.cf.gpoapp.validator.UserInsertValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final IUserService userService;
    private final RegionRepository regionRepository;
    private final UserInsertValidator userInsertValidator;

    /**
     * Αυτή η μέθοδος εκτελείται αυτόματα πριν από κάθε Get ή Post.
     * Προσθέτει τη λίστα "regions" στο Model, οπότε δεν χρειάζεται να την καλούμε
     * χειροκίνητα στα catch blocks.
     */
    @ModelAttribute("regions")
    public List<Region> getRegions() {
        return regionRepository.findAllByOrderByNameAsc();
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserInsertDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserInsertDTO userInsertDTO,
                               BindingResult bindingResult,
                               Model model) {

        // Εκτέλεση του custom validator για username, email, vat
        userInsertValidator.validate(userInsertDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            log.warn("Validation failed for user registration: {}", bindingResult.getAllErrors());
            return "register";
        }

        try {
            userService.registerUser(userInsertDTO);
            log.info("User registration successful for: {}", userInsertDTO.getUsername());
            // PRG Pattern: redirect μετά από επιτυχημένο Post
            return "redirect:/login?success";

        } catch (EntityAlreadyExistsException e) {
            log.warn("Registration attempt failed: {}", e.getMessage());
            bindingResult.rejectValue("username", "alreadyExists", e.getMessage());
            return "register";

        } catch (EntityNotFoundException e) {
            log.error("Critical error: Region not found during save: {}", e.getMessage());
            bindingResult.rejectValue("region", "notFound", "Επιλέξτε μια έγκυρη περιοχή.");
            return "register";

        } catch (Exception e) {
            log.error("Unexpected error during registration process", e);
            model.addAttribute("errorMessage", "Παρουσιάστηκε ένα απρόσμενο σφάλμα.");
            return "register";
        }
    }
}
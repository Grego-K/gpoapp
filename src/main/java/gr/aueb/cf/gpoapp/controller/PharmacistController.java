package gr.aueb.cf.gpoapp.controller;


import gr.aueb.cf.gpoapp.service.ICategoryService;
import gr.aueb.cf.gpoapp.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pharmacist")
@RequiredArgsConstructor
public class PharmacistController {

    private final IProductService productService;
    private final ICategoryService categoryService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "pharmacist/dashboard";
    }

    /* Η μέθοδος showProducts αφαιρέθηκε από εδώ.
       Η διαχείριση των προϊόντων γίνεται πλέον από τον ProductController στο path /products
       για την αποφυγή URL path conflicts και την ορθή λειτουργία του navigation.
    */
}
package gr.aueb.cf.gpoapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.service.IOrderService;
import gr.aueb.cf.gpoapp.service.ISupplierService;
import gr.aueb.cf.gpoapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pharmacist/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final ISupplierService supplierService;
    private final ObjectMapper objectMapper; // Για το parsing του JSON

    @GetMapping
    public String listOrders(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Order> orders = orderService.findAllOrdersByPharmacist(user);
        model.addAttribute("orders", orders);
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        return "pharmacist/orders";
    }

    /**
     * ΝΕΟ ENDPOINT: Μαζική προσθήκη προϊόντων από την προσωρινή λίστα του UI.
     */
    @PostMapping("/add-bulk")
    public String addBulkProductsToOrder(@RequestParam("itemsJson") String itemsJson,
                                         Principal principal,
                                         RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(principal.getName());

            // Μετατροπή του JSON String σε List από Maps
            List<Map<String, Object>> items = objectMapper.readValue(itemsJson, new TypeReference<>() {});

            // Προσθήκη κάθε προϊόντος στην παραγγελία
            for (Map<String, Object> item : items) {
                Long productId = Long.valueOf(item.get("productId").toString());
                Integer quantity = Integer.valueOf(item.get("quantity").toString());
                orderService.addProductToOrder(user, productId, quantity);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Τα προϊόντα προστέθηκαν επιτυχώς στην παραγγελία σας!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Σφάλμα κατά τη μαζική προσθήκη: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/{uuid}/submit")
    public String submitOrder(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        try {
            orderService.submitOrder(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Η παραγγελία υποβλήθηκε επιτυχώς!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Αποτυχία υποβολής: " + e.getMessage());
        }
        return "redirect:/pharmacist/orders";
    }

    @PostMapping("/{uuid}/cancel")
    public String cancelOrder(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Η παραγγελία ακυρώθηκε επιτυχώς.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Σφάλμα κατά την ακύρωση: " + e.getMessage());
        }
        return "redirect:/pharmacist/orders";
    }

    @GetMapping("/{uuid}")
    public String viewOrderDetails(@PathVariable String uuid, Model model) {
        Order order = orderService.findOrderByUuid(uuid);
        model.addAttribute("order", order);
        return "pharmacist/order-details";
    }
}
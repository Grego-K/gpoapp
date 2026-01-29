package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.dto.OrderItemRequestDTO;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.service.IOrderService;
import gr.aueb.cf.gpoapp.service.ISupplierService;
import gr.aueb.cf.gpoapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/pharmacist/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final ISupplierService supplierService;

    @GetMapping
    public String listOrders(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Order> orders = orderService.findAllOrdersByPharmacist(user);
        model.addAttribute("orders", orders);
        model.addAttribute("suppliers", supplierService.findAllSuppliers());
        return "pharmacist/orders";
    }

    /**
     * Μαζική προσθήκη προϊόντων:
     * Δέχεται JSON body από το fetch της JavaScript.
     * Χρησιμοποιεί το @RequestBody για αυτόματο mapping.
     */
    @PostMapping("/add-bulk")
    @ResponseBody
    public ResponseEntity<String> addBulkProductsToOrder(@RequestBody List<OrderItemRequestDTO> items,
                                                         Principal principal) {
        try {
            // Ταυτοποίηση χρήστη
            User user = userService.findByUsername(principal.getName());

            // Προσθήκη κάθε προϊόντος στην παραγγελία μέσω του Service
            for (OrderItemRequestDTO item : items) {
                orderService.addProductToOrder(user, item.getProductId(), item.getQuantity());
            }

            // Επιστρέφουμε 200 OK για να το διαβάσει η JavaScript (response.ok)
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            // Επιστρέφουμε 500 αν κάτι πάει στραβά στο server side
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Σφάλμα κατά τη μαζική προσθήκη: " + e.getMessage());
        }
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
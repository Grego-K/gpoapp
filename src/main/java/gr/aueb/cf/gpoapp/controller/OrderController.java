package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.core.filters.OrderFilters;
import gr.aueb.cf.gpoapp.dto.OrderItemRequestDTO;
import gr.aueb.cf.gpoapp.dto.OrderReadOnlyDTO;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.service.IOrderService;
import gr.aueb.cf.gpoapp.service.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Διαχειρίζεται τις λειτουργίες των παραγγελιών για τον Φαρμακοποιό.
 */
@Controller
@RequestMapping("/pharmacist/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final ISupplierService supplierService;

    /**
     * Εμφανίζει τη λίστα παραγγελιών του συνδεδεμένου φαρμακοποιού.
     * Χρησιμοποιεί το GenericFilters για DESC ταξινόμηση (νεότερες πρώτα).
     */
    @GetMapping
    public String listOrders(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            OrderFilters filters,
            Model model) {

        filters.setPageable(PageRequest.of(page, size));
        Page<OrderReadOnlyDTO> ordersPage = orderService.findAllOrdersDTOByPharmacist(user, filters);

        model.addAttribute("orders", ordersPage);
        model.addAttribute("filters", filters);
        model.addAttribute("suppliers", supplierService.findAllSuppliers());

        return "pharmacist/orders";
    }

    /**
     * Εμφανίζει τις λεπτομέρειες μιας συγκεκριμένης παραγγελίας.
     */
    @GetMapping("/{uuid}")
    public String viewOrder(@PathVariable String uuid, Model model) {
        OrderReadOnlyDTO order = orderService.findOrderDTOByUuid(uuid);
        model.addAttribute("order", order);
        return "pharmacist/order-details";
    }

    /**
     * Οριστική υποβολή της παραγγελίας (από PENDING σε SUBMITTED).
     */
    @PostMapping("/{uuid}/submit")
    public String submitOrder(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        try {
            orderService.submitOrder(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Η παραγγελία υποβλήθηκε επιτυχώς!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Σφάλμα κατά την υποβολή: " + e.getMessage());
        }
        return "redirect:/pharmacist/orders";
    }

    /**
     * Ακύρωση μιας εκκρεμούς παραγγελίας.
     */
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

    /**
     * REST endpoint για τη μαζική προσθήκη προϊόντων από το καλάθι (localStorage).
     */
    @PostMapping("/add-bulk")
    @ResponseBody
    public ResponseEntity<?> addBulkOrder(@AuthenticationPrincipal User user,
                                          @RequestBody List<OrderItemRequestDTO> items) {
        try {
            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body("Το καλάθι είναι άδειο.");
            }

            for (OrderItemRequestDTO item : items) {
                orderService.addProductToOrder(user, item.getProductId(), item.getQuantity());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Σφάλμα συστήματος: " + e.getMessage());
        }
    }
}
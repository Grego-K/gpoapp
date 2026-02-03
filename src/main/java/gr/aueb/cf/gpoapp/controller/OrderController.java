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

@Controller
@RequestMapping("/pharmacist/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final ISupplierService supplierService;

    // Λίστα παραγγελιών
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

    // Λεπτομέρειες παραγγελίας
    @GetMapping("/{uuid}")
    public String viewOrder(@PathVariable String uuid, Model model) {
        OrderReadOnlyDTO order = orderService.findOrderDTOByUuid(uuid);
        model.addAttribute("order", order);
        return "pharmacist/order-details";
    }

    // Οριστική Υποβολή (Submission)
    @PostMapping("/{uuid}/submit")
    public String submitOrder(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        try {
            orderService.submitOrder(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Η παραγγελία υποβλήθηκε επιτυχώς!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/pharmacist/orders";
    }

    // Ακύρωση παραγγελίας
    @PostMapping("/{uuid}/cancel")
    public String cancelOrder(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Η παραγγελία ακυρώθηκε.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/pharmacist/orders";
    }

    // Προσθήκη Bulk από το καλάθι
    @PostMapping("/add-bulk")
    @ResponseBody
    public ResponseEntity<?> addBulkOrder(@AuthenticationPrincipal User user,
                                          @RequestBody List<OrderItemRequestDTO> items) {
        try {
            if (items == null || items.isEmpty()) return ResponseEntity.badRequest().body("Άδειο καλάθι");

            for (OrderItemRequestDTO item : items) {
                orderService.addProductToOrder(user, item.getProductId(), item.getQuantity());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
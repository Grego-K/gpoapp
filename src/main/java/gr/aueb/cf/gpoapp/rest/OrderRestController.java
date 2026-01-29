package gr.aueb.cf.gpoapp.rest;

import gr.aueb.cf.gpoapp.dto.OrderItemReadOnlyDTO;
import gr.aueb.cf.gpoapp.dto.OrderReadOnlyDTO;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.service.IOrderService;
import gr.aueb.cf.gpoapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller για τη διαχείριση των παραγγελιών.
 * Παρέχει endpoints για την ανάκτηση ιστορικού και λεπτομερειών παραγγελίας.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderService orderService;
    private final IUserService userService;

    // Επιστρέφει όλες τις παραγγελίες του συνδεδεμένου Φαρμακοποιού.
    @GetMapping
    public ResponseEntity<List<OrderReadOnlyDTO>> getMyOrders(Principal principal) {
        try {
            User user = userService.findByUsername(principal.getName());
            List<Order> orders = orderService.findAllOrdersByPharmacist(user);

            List<OrderReadOnlyDTO> dtos = orders.stream()
                    .map(this::mapToOrderDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Επιστρέφει τις λεπτομέρειες μιας παραγγελίας βάσει UUID
    @GetMapping("/{uuid}")
    public ResponseEntity<OrderReadOnlyDTO> getOrderByUuid(@PathVariable String uuid) {
        try {
            Order order = orderService.findOrderByUuid(uuid);
            if (order == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(mapToOrderDTO(order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Helper μέθοδος για το Mapping από Order Entity σε OrderReadOnlyDTO.
     * Αφαίρεση του .doubleValue() για συμβατότητα με BigDecimal.
     * BigDecimal για ακρίβεια.
     */
    private OrderReadOnlyDTO mapToOrderDTO(Order order) {
        return OrderReadOnlyDTO.builder()
                .uuid(order.getUuid())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "N/A")
                .items(order.getOrderItems().stream()
                        .map(item -> OrderItemReadOnlyDTO.builder()
                                .productName(item.getProduct() != null ? item.getProduct().getProductName() : "Άγνωστο Προϊόν")
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
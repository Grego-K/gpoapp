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
 * Παρέχει endpoints διάφορες δυνατότητες οπως GET, POST klp.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderService orderService;
    private final IUserService userService;

    /**
     * Επιστρέφει όλες τις παραγγελίες του συνδεδεμένου Φαρμακοποιού.
     * Χρησιμοποιεί το Principal για να ταυτοποιήσει τον χρήστη από το JWT.
     */
    @GetMapping
    public ResponseEntity<List<OrderReadOnlyDTO>> getMyOrders(Principal principal) {
        try {
            // Ανάκτηση του User Entity βάσει του username από το JWT (Principal)
            User user = userService.findByUsername(principal.getName());

            // Ανάκτηση των παραγγελιών από το Service
            List<Order> orders = orderService.findAllOrdersByPharmacist(user);

            // Μετατροπή των Entities σε ReadOnlyDTOs
            List<OrderReadOnlyDTO> dtos = orders.stream()
                    .map(this::mapToOrderDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            // Επιστροφή 500 Internal Server Error σε περίπτωση απρόβλεπτου σφάλματος
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Επιστρέφει τις λεπτομέρειες μιας συγκεκριμένης παραγγελίας βάσει UUID.
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<OrderReadOnlyDTO> getOrderByUuid(@PathVariable String uuid) {
        try {
            Order order = orderService.findOrderByUuid(uuid);
            return ResponseEntity.ok(mapToOrderDTO(order));
        } catch (Exception e) {
            // Επιστροφή 404 αν το UUID δεν αντιστοιχεί σε παραγγελία
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Helper μέθοδος για το Mapping από Order Entity σε OrderReadOnlyDTO.
     * ΔΙΟΡΘΩΣΗ: Αφαίρεση του .doubleValue() για συμβατότητα με BigDecimal.
     */
    private OrderReadOnlyDTO mapToOrderDTO(Order order) {
        return OrderReadOnlyDTO.builder()
                .uuid(order.getUuid())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount()) // BigDecimal απευθείας ανάθεση
                .createdAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "N/A")
                .items(order.getOrderItems().stream()
                        .map(item -> OrderItemReadOnlyDTO.builder()
                                .productName(item.getProduct() != null ? item.getProduct().getProductName() : "Unknown Product")
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice()) // ΔΙΟΡΘΩΣΗ ΕΔΩ: Περνάμε το BigDecimal ως έχει
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
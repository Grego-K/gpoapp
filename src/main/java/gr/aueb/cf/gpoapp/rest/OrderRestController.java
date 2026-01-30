package gr.aueb.cf.gpoapp.rest;

import gr.aueb.cf.gpoapp.core.filters.OrderFilters;
import gr.aueb.cf.gpoapp.dto.OrderReadOnlyDTO;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.service.IOrderService;
import gr.aueb.cf.gpoapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller για τη διαχείριση των παραγγελιών.
 * Παρέχει endpoints για την ανάκτηση ιστορικού και λεπτομερειών παραγγελίας.
 */
@RestController
@RequestMapping("/api/pharmacist/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderService orderService;
    private final IUserService userService;

    // Επιστρέφει όλες τις παραγγελίες του συνδεδεμένου Φαρμακοποιού.
    @GetMapping
    public ResponseEntity<List<OrderReadOnlyDTO>> getMyOrders(Principal principal, OrderFilters filters) {
        try {
            User user = userService.findByUsername(principal.getName());

            // Κλήση του service με 2 ορίσματα findAllOrdersByPharmacist(user, filters) και λήψη Page αντικειμένου
            Page<Order> orderPage = orderService.findAllOrdersByPharmacist(user, filters);

            List<OrderReadOnlyDTO> dtos = orderPage.getContent().stream()
                    .map(this::mapToReadOnlyDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Helper μέθοδος για το Mapping από Order Entity σε OrderReadOnlyDTO.
     * Αφαίρεση του .doubleValue() για συμβατότητα με BigDecimal.
     * BigDecimal για ακρίβεια.
     */
    private OrderReadOnlyDTO mapToReadOnlyDTO(Order order) {
        // Χρησιμοποιούμε τον Builder με τα πεδία που προσθέσαμε στο DTO
        return OrderReadOnlyDTO.builder()
                .id(order.getId())
                .uuid(order.getUuid())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .supplierNames(order.getDistinctSuppliers())
                .itemCount(order.getOrderItems().size())
                .build();
    }
}
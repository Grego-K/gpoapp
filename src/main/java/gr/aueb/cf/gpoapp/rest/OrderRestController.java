package gr.aueb.cf.gpoapp.rest;

import gr.aueb.cf.gpoapp.core.filters.OrderFilters;
import gr.aueb.cf.gpoapp.dto.OrderReadOnlyDTO;
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

            // Κλήση της DTO μεθόδου του service που χρησιμοποιεί εσωτερικά τον OrderMapper
            Page<OrderReadOnlyDTO> orderDtoPage = orderService.findAllOrdersDTOByPharmacist(user, filters);

            return ResponseEntity.ok(orderDtoPage.getContent());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
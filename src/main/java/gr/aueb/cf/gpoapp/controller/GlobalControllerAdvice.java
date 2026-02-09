package gr.aueb.cf.gpoapp.controller;

import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import gr.aueb.cf.gpoapp.repository.OrderRepository;
import gr.aueb.cf.gpoapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final OrderRepository orderRepository;
    private final IUserService userService;

    @ModelAttribute("activeOrder")
    public Order getActiveOrder(Principal principal) {
        if (principal == null) return null;

        try {
            User user = userService.findByUsername(principal.getName());

            /**
             * Καλούμε τη μέθοδο με το FETCH join για να είναι διαθέσιμα τα orderItems
             * στο badge του layout.html.
             */
            return orderRepository.findByUserAndStatusWithItems(user, OrderStatus.PENDING)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error retrieving active order for header: {}", e.getMessage());
            return null;
        }
    }
}
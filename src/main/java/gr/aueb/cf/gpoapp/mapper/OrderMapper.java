package gr.aueb.cf.gpoapp.mapper;

import gr.aueb.cf.gpoapp.dto.OrderItemReadOnlyDTO;
import gr.aueb.cf.gpoapp.dto.OrderReadOnlyDTO;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.OrderItem;
import gr.aueb.cf.gpoapp.service.RebateSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final RebateSettlementService rebateSettlementService;

    /**
     * Μετατρέπει το Order Entity σε ReadOnly DTO εμπλουτισμένο με Rebates.
     */
    public OrderReadOnlyDTO mapToOrderReadOnlyDTO(Order order) {
        if (order == null) return null;

        // Μετατροπή των επιμέρους αντικειμένων (Items)
        List<OrderItemReadOnlyDTO> itemDTOs = order.getOrderItems().stream()
                .map(this::mapToOrderItemReadOnlyDTO)
                .collect(Collectors.toList());

        // Υπολογισμός συνολικού Rebate για όλη την παραγγελία
        BigDecimal totalRebate = itemDTOs.stream()
                .map(OrderItemReadOnlyDTO::getEstimatedItemRebate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderReadOnlyDTO.builder()
                .id(order.getId())
                .uuid(order.getUuid())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .itemCount(order.getOrderItems().size())
                // Συλλέγουμε τα ονόματα των προμηθευτών (αν υπάρχουν πολλοί στην παραγγελία)
                .supplierNames(order.getOrderItems().stream()
                        .map(item -> item.getProduct().getSupplier().getCompanyName())
                        .distinct()
                        .collect(Collectors.toList()))
                .items(itemDTOs)
                .totalEstimatedRebate(totalRebate)
                .build();
    }

    /**
     * Μετατρέπει ένα OrderItem Entity σε ReadOnly DTO.
     */
    private OrderItemReadOnlyDTO mapToOrderItemReadOnlyDTO(OrderItem item) {
        // Καλούμε το service για τον υπολογισμό του συνολικού rebate για τη συγκεκριμένη ποσότητα
        BigDecimal totalEstimatedRebate = rebateSettlementService.calculateItemRebate(item);

        // Υπολογισμός Rebate ανά τεμάχιο (Σύνολο Rebate / Ποσότητα)
        BigDecimal rebatePerUnit = BigDecimal.ZERO;
        if (item.getQuantity() > 0) {
            rebatePerUnit = totalEstimatedRebate.divide(BigDecimal.valueOf(item.getQuantity()), 2, RoundingMode.HALF_UP);
        }

        // Υπολογισμός Καθαρής Τιμής Tier (Τιμή GPO - Rebate ανά τεμάχιο)
        // Αν το rebate είναι 0, το netTierPrice θα παραμείνει null για να δείξουμε "-" στο UI
        BigDecimal netTierPrice = null;
        if (totalEstimatedRebate.compareTo(BigDecimal.ZERO) > 0) {
            netTierPrice = item.getUnitPrice().subtract(rebatePerUnit);
        }

        return OrderItemReadOnlyDTO.builder()
                .productName(item.getProduct().getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())  // Snapshot της τιμής
                .estimatedItemRebate(totalEstimatedRebate)
                .netTierPrice(netTierPrice) // Η "καθαρή" τιμή μετά την έκπτωση
                .build();
    }
}
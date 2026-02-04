package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.filters.OrderFilters;
import gr.aueb.cf.gpoapp.dto.OrderReadOnlyDTO;
import gr.aueb.cf.gpoapp.mapper.OrderMapper;
import gr.aueb.cf.gpoapp.model.*;
import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import gr.aueb.cf.gpoapp.model.enums.PeriodType;
import gr.aueb.cf.gpoapp.repository.OrderRepository;
import gr.aueb.cf.gpoapp.repository.ProductProgressRepository;
import gr.aueb.cf.gpoapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductProgressRepository progressRepository; // Προσθήκη για το Rebate System
    private final OrderMapper orderMapper; // Inject τον Mapper για το real-time rebate calculation

    // Επιστρέφει τις παραγγελίες σε σελίδες, εφαρμόζοντας φίλτρα (status, date, supplier)
    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAllOrdersByPharmacist(User user, OrderFilters filters) {
        // Μετατροπή του LocalDate σε LocalDateTime
        LocalDateTime startDateTime = (filters.getDateFrom() != null)
                ? filters.getDateFrom().atStartOfDay()
                : null;

        // Κλήση της getPageable() η οποία περιλαμβάνει το DESC sorting από την GenericFilters
        return orderRepository.findFilteredOrders(
                user,
                filters.getStatus(),
                startDateTime,
                filters.getSupplierId(),
                filters.getPageable()
        );
    }

    /**
     * Υλοποίηση που επιστρέφει DTOs.
     * Εδώ γίνεται η κλήση του Mapper ο οποίος χρησιμοποιεί το RebateSettlementService
     * για να υπολογίσει on-the-fly την επιστροφή χρημάτων.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderReadOnlyDTO> findAllOrdersDTOByPharmacist(User user, OrderFilters filters) {
        Page<Order> ordersPage = findAllOrdersByPharmacist(user, filters);
        return ordersPage.map(orderMapper::mapToOrderReadOnlyDTO);
    }

    // Επιστρέφει τις λεπτομέρειες μιας παραγγελίας χρησιμοποιώντας Deep Fetch
    @Override
    @Transactional(readOnly = true)
    public Order findOrderByUuid(String uuid) {
        return orderRepository.findByUuidWithItems(uuid)
                .orElseThrow(() -> new RuntimeException("Η παραγγελία με UUID: " + uuid + " δεν βρέθηκε"));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderReadOnlyDTO findOrderDTOByUuid(String uuid) {
        Order order = findOrderByUuid(uuid);
        return orderMapper.mapToOrderReadOnlyDTO(order);
    }

    // Προσθήκη προϊόντος σε active order με JOIN FETCH των orderItems
    @Override
    @Transactional
    public void addProductToOrder(User user, Long productId, Integer quantity) {
        Order activeOrder = orderRepository.findByUserAndStatusWithItems(user, OrderStatus.PENDING)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(OrderStatus.PENDING);
                    return orderRepository.save(newOrder);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Το προϊόν δεν βρέθηκε"));

        Optional<OrderItem> existingItem = activeOrder.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getGpoPrice()); // Snapshot της τρέχουσας τιμής GPO
            activeOrder.addOrderItem(newItem);
        }

        orderRepository.save(activeOrder);
    }

    // Υποβολή της παραγγελίας και αλλαγή κατάστασης σε submitted
    @Override
    @Transactional
    public void submitOrder(String uuid) {
        Order order = findOrderByUuid(uuid);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Μόνο εκκρεμείς παραγγελίες μπορούν να υποβληθούν.");
        }

        order.setStatus(OrderStatus.SUBMITTED);

        // Ενημέρωση του Rebate Progress (Volume) για κάθε προϊόν της παραγγελίας
        updateProductProgressAfterSubmission(order);

        orderRepository.save(order);
    }

    // Ακύρωση της παραγγελίας και αλλαγή κατάστασης σε cancelled
    @Override
    @Transactional
    public void cancelOrder(String uuid) {
        Order order = findOrderByUuid(uuid);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Μόνο εκκρεμείς παραγγελίες μπορούν να ακυρωθούν.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    // Ενημερώνει το ProductProgress για κάθε προϊόν της παραγγελίας.
    private void updateProductProgressAfterSubmission(Order order) {
        String currentPeriod = getCurrentPeriodLabel();

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();

            ProductProgress progress = progressRepository
                    .findByProductIdAndPeriodLabel(product.getId(), currentPeriod)
                    .orElseGet(() -> {
                        ProductProgress newProgress = new ProductProgress();
                        newProgress.setProduct(product);
                        newProgress.setPeriodLabel(currentPeriod);
                        newProgress.setPeriodType(PeriodType.QUARTER);
                        newProgress.setVolume(0);
                        return newProgress;
                    });

            progress.setVolume(progress.getVolume() + item.getQuantity());
            progressRepository.save(progress);
        }
    }

    private String getCurrentPeriodLabel() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int quarter = (month - 1) / 3 + 1;
        return year + "_Q" + quarter;
    }
}
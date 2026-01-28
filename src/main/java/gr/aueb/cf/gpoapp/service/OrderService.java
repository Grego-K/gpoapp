package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.OrderItem;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import gr.aueb.cf.gpoapp.repository.OrderRepository;
import gr.aueb.cf.gpoapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // Επιστρέφει όλες τις παραγγελίες με Deep Fetch για να αποφευχθεί το LazyInitializationException
    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllOrdersByPharmacist(User user) {
        return orderRepository.findByUserWithItemsOrderByCreatedAtDesc(user);
    }

    //Επιστρέφει τις λεπτομέρειες μιας παραγγελίας χρησιμοποιώντας Deep Fetch
    @Override
    @Transactional(readOnly = true)
    public Order findOrderByUuid(String uuid) {
        return orderRepository.findByUuidWithItems(uuid)
                .orElseThrow(() -> new RuntimeException("Η παραγγελία με UUID: " + uuid + " δεν βρέθηκε"));
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
}
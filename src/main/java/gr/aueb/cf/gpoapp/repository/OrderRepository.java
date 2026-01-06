package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUuid(String uuid);

    // Ιστορικό παραγγελιών χρήστη
    List<Order> findByUserId(Long userId);

    // Ευρεση με status (Pending, Completed, Cancelled)
    List<Order> findByStatus(OrderStatus status);

    // Παραγγελίες ανα χρονικά διαστήματα
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}

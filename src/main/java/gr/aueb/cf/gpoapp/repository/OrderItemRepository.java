package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Εύρεση όλων των προϊόντων μιας συγκεκριμένης παραγγελίας
    List<OrderItem> findByOrderId(Long orderId);
}
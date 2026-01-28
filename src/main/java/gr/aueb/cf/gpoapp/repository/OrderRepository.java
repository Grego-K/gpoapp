package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Deep Fetch για το logic του "Active Order" (Καλάθι).
     * Φέρνει Order -> Items -> Product ώστε να ελέγξουμε αν ένα προϊόν υπάρχει ήδη.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "WHERE o.user = :user AND o.status = :status")
    Optional<Order> findByUserAndStatusWithItems(@Param("user") User user, @Param("status") OrderStatus status);

    /**
     * Deep Fetch για τη λίστα ιστορικού (orders.html).
     * Φέρνει Order -> Items -> Product -> Supplier για να εμφανιστούν τα badges των προμηθευτών.
     */
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH p.supplier " +
            "WHERE o.user = :user ORDER BY o.createdAt DESC")
    List<Order> findByUserWithItemsOrderByCreatedAtDesc(@Param("user") User user);

    /**
     * Deep Fetch για τη σελίδα λεπτομερειών (order-details.html).
     * Φέρνει Order -> User (για στοιχεία παράδοσης) και Items -> Product -> Supplier.
     */
    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH p.supplier " +
            "WHERE o.uuid = :uuid")
    Optional<Order> findByUuidWithItems(@Param("uuid") String uuid);
}
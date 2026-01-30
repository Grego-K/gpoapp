package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
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

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH p.supplier " +
            "WHERE o.uuid = :uuid")
    Optional<Order> findByUuidWithItems(@Param("uuid") String uuid);

    /**
     * Φιλτράρισμα με Pagination και FETCH JOIN για αποφυγή LazyInitializationException.
     * Χρησιμοποιούμε countQuery γιατί το FETCH JOIN μπερδεύει το αυτόματο count του Spring Data.
     */
    @Query(value = "SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH p.supplier s " +
            "WHERE o.user = :user " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:dateFrom IS NULL OR o.createdAt >= :dateFrom) " +
            "AND (:supplierId IS NULL OR p.supplier.id = :supplierId)",
            countQuery = "SELECT COUNT(DISTINCT o) FROM Order o " +
                    "JOIN o.orderItems oi " +
                    "JOIN oi.product p " +
                    "WHERE o.user = :user " +
                    "AND (:status IS NULL OR o.status = :status) " +
                    "AND (:dateFrom IS NULL OR o.createdAt >= :dateFrom) " +
                    "AND (:supplierId IS NULL OR p.supplier.id = :supplierId)")
    Page<Order> findFilteredOrders(@Param("user") User user,
                                   @Param("status") OrderStatus status,
                                   @Param("dateFrom") LocalDateTime dateFrom,
                                   @Param("supplierId") Long supplierId,
                                   Pageable pageable);
}
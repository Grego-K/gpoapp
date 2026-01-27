package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Επιστρέφει ένα προϊόν με την κατηγορία και τον προμηθευτή φορτωμένα (Eager).
     */
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.supplier WHERE p.id = :id")
    Optional<Product> findByIdWithRelations(@Param("id") Long id);

    /**
     * Επιστρέφει σελιδοποιημένα προϊόντα με προαιρετικά φίλτρα.
     */
    @Query("""
            SELECT p FROM Product p
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.supplier
            WHERE (:name IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:catId IS NULL OR p.category.id = :catId)
           """)
    Page<Product> findFiltered(
            @Param("name") String name,
            @Param("catId") Long catId,
            Pageable pageable
    );

    /**
     * Επιστρέφει όλα τα προϊόντα με φορτωμένες τις βασικές συσχετίσεις.
     */
    @Query("""
            SELECT p FROM Product p
            LEFT JOIN FETCH p.category
            LEFT JOIN FETCH p.supplier
           """)
    List<Product> findAllWithRelations();

    // Αναζήτηση προϊόντος βάσει UUID
    Optional<Product> findByUuid(String uuid);

    // Αναζήτηση προϊόντων βάσει ονόματος (case-insensitive)
    List<Product> findByProductNameContainingIgnoreCase(
            String productName,
            Pageable pageable
    );

    // Προϊόντα ανά προμηθευτή
    List<Product> findBySupplierId(Long supplierId);

    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    // Προιόντα ανά κατηγορία
    List<Product> findByCategoryId(Long categoryId);
}
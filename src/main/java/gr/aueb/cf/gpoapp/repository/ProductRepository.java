package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByUuid(String uuid);

    // Αναζήτηση προϊόντων με μέρος του ονόματος (π.χ. "depon" θα φέρει "DEPON 100mg")
    List<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    // Εύρεση όλων των προϊόντων ενός προμηθευτή
    List<Product> findBySupplierId(Long supplierId);

    // Προϊόντα που είναι σε απόθεμα
    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    // Εύρεση προϊόντων βάσει κατηγορίας
    List<Product> findByCategoryId(Long categoryId);
}
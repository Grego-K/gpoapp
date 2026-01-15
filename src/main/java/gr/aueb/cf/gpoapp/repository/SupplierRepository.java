package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByUuid(String uuid);
    Optional<Supplier> findByVat(String vat);

    // Ελεγχος αν υπάρχει ήδη ο προμηθευτής
    boolean existsByVat(String vat);

    // Εύρεση προμηθευτών σε μια συγκεκριμένη περιφέρεια
    List<Supplier> findByRegionId(Long regionId);
}

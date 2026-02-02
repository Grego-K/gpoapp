package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.ProductProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductProgressRepository extends JpaRepository<ProductProgress, Long> {

    /**
     * Βρίσκει την πρόοδο ενός προϊόντος για μια συγκεκριμένη περίοδο (π.χ. 2026_Q1)
     */
    Optional<ProductProgress> findByProductIdAndPeriodLabel(Long productId, String periodLabel);

    /**
     * Fetch Join για να φέρουμε και το προϊόν μαζί αν το χρειαστούμε,
     * αποφεύγοντας τα πολλαπλά queries (N+1 problem).
     */
    @Query("SELECT pp FROM ProductProgress pp JOIN FETCH pp.product WHERE pp.product.id = :productId AND pp.periodLabel = :periodLabel")
    Optional<ProductProgress> findByProductAndPeriodWithFetch(@Param("productId") Long productId, @Param("periodLabel") String periodLabel);
}

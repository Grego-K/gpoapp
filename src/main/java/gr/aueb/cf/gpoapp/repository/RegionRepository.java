package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.static_data.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByName(String name);

    // Αλφαβητική ταξινόμηση
    List<Region> findAllByOrderByNameAsc();
}

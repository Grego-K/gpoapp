package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.Capability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapabilityRepository extends JpaRepository<Capability, Long> {
}
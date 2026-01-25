package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Αναζήτηση βάσει Username
    Optional<User> findByUsername(String username);

    // Αναζήτηση βάσει Email
    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(String uuid);

    // Αναζήτηση βάσει Vat
    Optional<User> findByVat(String vat);

    List<User> findByRegionId(Long id, Pageable pageable);

    List<User> findByRole(Role role);

    // Βοηθητικά για validation στο Service Layer
    boolean existsByUsername(String username);
    boolean existsByVat(String vat);
    boolean existsByEmail(String email);
}
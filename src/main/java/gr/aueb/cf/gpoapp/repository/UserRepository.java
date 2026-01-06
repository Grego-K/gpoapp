package gr.aueb.cf.gpoapp.repository;

import gr.aueb.cf.gpoapp.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);

    Optional<User> findByVat(String vat);

    Optional<User> findByEmail(String email);

    List<User> findByRegionId(Long id, Pageable pageable);

    // Βοηθητικά για validation στο Service Layer
    boolean existsByVat(String vat);
    boolean existsByEmail(String email);
}
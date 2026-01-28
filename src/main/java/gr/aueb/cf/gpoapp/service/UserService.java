package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.gpoapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.mapper.UserMapper;
import gr.aueb.cf.gpoapp.model.Role;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.static_data.Region;
import gr.aueb.cf.gpoapp.repository.RegionRepository;
import gr.aueb.cf.gpoapp.repository.RoleRepository;
import gr.aueb.cf.gpoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        log.info("Attempting to find user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username '" + username + "' not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(UserInsertDTO userInsertDTO)
            throws EntityAlreadyExistsException, EntityNotFoundException {

        try {
            // Έλεγχος μοναδικότητας Username
            if (userRepository.findByUsername(userInsertDTO.getUsername()).isPresent()) {
                throw new EntityAlreadyExistsException("User", "Username '" + userInsertDTO.getUsername() + "' already exists");
            }

            // Έλεγχος μοναδικότητας Email
            if (userRepository.findByEmail(userInsertDTO.getEmail()).isPresent()) {
                throw new EntityAlreadyExistsException("User", "Email '" + userInsertDTO.getEmail() + "' already exists");
            }

            // Έλεγχος μοναδικότητας VAT
            if (userRepository.findByVat(userInsertDTO.getVat()).isPresent()) {
                throw new EntityAlreadyExistsException("User", "User with VAT " + userInsertDTO.getVat() + " already exists");
            }

            // Mapping DTO -> Entity
            User user = userMapper.mapToUserEntity(userInsertDTO);

            // Κρυπτογράφηση κωδικού πριν την αποθήκευση
            user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));

            Role role = roleRepository.findByName("PHARMACIST")
                    .orElseThrow(() -> new EntityNotFoundException("Role", "Role PHARMACIST not found in database"));
            user.setRole(role);

            // Εύρεση και σύνδεση Region
            Region region = regionRepository.findById(userInsertDTO.getRegion())
                    .orElseThrow(() -> new EntityNotFoundException("Region", "Invalid region id: " + userInsertDTO.getRegion()));
            user.setRegion(region);

            // Αποθήκευση
            User savedUser = userRepository.save(user);

            // Logging επιτυχίας
            log.info("User with username={} and vat={} saved with dynamic role PHARMACIST.", userInsertDTO.getUsername(), userInsertDTO.getVat());

            return savedUser;

        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error("Registration failed: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("An unexpected error occurred during user registration", e);
            throw e;
        }
    }
}
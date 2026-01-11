package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.gpoapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.mapper.UserMapper;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.model.static_data.Region;
import gr.aueb.cf.gpoapp.repository.RegionRepository;
import gr.aueb.cf.gpoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // log
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final UserMapper userMapper;

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

            // Εύρεση και σύνδεση Region
            Region region = regionRepository.findById(userInsertDTO.getRegion())
                    .orElseThrow(() -> new EntityNotFoundException("Region", "Invalid region id: " + userInsertDTO.getRegion()));
            user.setRegion(region);

            // Αποθήκευση
            User savedUser = userRepository.save(user);

            // Logging για επιτυχή εγγραφή ( structured logging )
            log.info("User with username={} and vat={} saved.", userInsertDTO.getUsername(), userInsertDTO.getVat());

            return savedUser;

        } catch (EntityAlreadyExistsException e) {
            // Logging του σφάλματος για log analyzers ( ELK Stack κλπ )
            log.error("Registration failed: User with vat={} already exists", userInsertDTO.getVat(), e);
            throw e;

        } catch (EntityNotFoundException e) {
            log.error("Registration failed: Region with id={} not found", userInsertDTO.getRegion(), e);
            throw e;

        } catch (Exception e) {
            // Catch-all για απρόσμενα σφάλματα (database down κλπ)
            log.error("An unexpected error occurred during user registration", e);
            throw e;
        }
    }
}
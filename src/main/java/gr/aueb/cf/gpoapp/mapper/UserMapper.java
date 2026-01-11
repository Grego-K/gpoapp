package gr.aueb.cf.gpoapp.mapper;

import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Μετατρέπει το DTO σε Entity.
     * Σημείωση: Το Region δεν τίθεται εδώ, καθώς απαιτεί αναζήτηση στη βάση
     * μέσω του Service layer.
     */
    public User mapToUserEntity(UserInsertDTO userInsertDTO) {
        User user = new User();

        user.setUsername(userInsertDTO.getUsername());
        user.setPassword(userInsertDTO.getPassword());
        user.setFirstname(userInsertDTO.getFirstname());
        user.setLastname(userInsertDTO.getLastname());
        user.setEmail(userInsertDTO.getEmail());
        user.setVat(userInsertDTO.getVat());
        user.setPhoneNumber(userInsertDTO.getPhonenumber());

        return user;
    }
}
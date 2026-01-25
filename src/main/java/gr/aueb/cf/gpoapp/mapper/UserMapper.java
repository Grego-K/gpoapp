package gr.aueb.cf.gpoapp.mapper;

import gr.aueb.cf.gpoapp.dto.UserInsertDTO;
import gr.aueb.cf.gpoapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUserEntity(UserInsertDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .vat(dto.getVat())
                .phoneNumber(dto.getPhonenumber())
                .build();
    }
}
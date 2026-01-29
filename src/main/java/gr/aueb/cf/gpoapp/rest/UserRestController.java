package gr.aueb.cf.gpoapp.rest;

import gr.aueb.cf.gpoapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.gpoapp.model.User;
import gr.aueb.cf.gpoapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserReadOnlyDTO> getMyProfile(Principal principal) {
        User user = userService.findByUsername(principal.getName());

        UserReadOnlyDTO dto = UserReadOnlyDTO.builder()
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .vat(user.getVat())
                .role(user.getRole().getName())
                .build();

        return ResponseEntity.ok(dto);
    }
}
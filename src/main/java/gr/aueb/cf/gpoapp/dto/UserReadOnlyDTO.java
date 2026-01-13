package gr.aueb.cf.gpoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String uuid;
    private String vat;
    private String region;
    private String username;
    private String role;
}
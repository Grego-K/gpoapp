package gr.aueb.cf.gpoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Read-Only DTO για την αναπαράσταση του προφίλ χρήστη
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReadOnlyDTO {
    private Long id;
    private String uuid;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String vat;
    private String region;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
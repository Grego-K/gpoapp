package gr.aueb.cf.gpoapp.rest;

import gr.aueb.cf.gpoapp.dto.AuthenticationRequest;
import gr.aueb.cf.gpoapp.dto.AuthenticationResponse;
import gr.aueb.cf.gpoapp.core.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            // Προσπάθεια authentication
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Αν πετύχει το authentication, προχωράμε στην έκδοση του token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            final String jwtToken = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build());

        } catch (BadCredentialsException e) {
            // Αν τα στοιχεία είναι λάθος, γυρνάμε 401 αντί για 500
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Λάθος όνομα χρήστη ή κωδικός πρόσβασης");
        } catch (Exception e) {
            // Για οποιοδήποτε άλλο σφάλμα
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Παρουσιάστηκε ένα σφάλμα στο σύστημα");
        }
    }
}
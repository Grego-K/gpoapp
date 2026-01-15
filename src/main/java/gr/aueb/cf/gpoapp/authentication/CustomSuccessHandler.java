package gr.aueb.cf.gpoapp.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Παίρνουμε τα roles (authorities) του συνδεδεμένου χρήστη
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = null;


        // Redirect για κάθε ρόλο στο κεντρικό του dashboard
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/admin/dashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("PHARMACIST")) {
                redirectUrl = "/pharmacist/dashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("SUPPLIER")) {
                redirectUrl = "/supplier/dashboard";
                break;
            }
        }

        // Αν δεν βρέθηκε ρόλος, redirect στην αρχική σελίδα
        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        // Εκτέλεση του redirect στο τελικό URL
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
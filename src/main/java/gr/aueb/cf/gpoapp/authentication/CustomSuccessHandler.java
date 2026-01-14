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

        // Έλεγχος ρόλου για το αντίστοιχο redirect
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("PHARMACIST")) {
                redirectUrl = "/pharmacist/dashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("SUPPLIER")) {
                redirectUrl = "/supplier/dashboard";
                break;
            }
        }

        // Αν δεν βρέθηκε, redirect την αρχική
        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
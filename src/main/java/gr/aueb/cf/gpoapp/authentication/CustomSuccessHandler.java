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
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Παίρνουμε τα roles του συνδεδεμένου χρήστη
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = null;

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin/dashboard"; //
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PHARMACIST"))) {
            redirectUrl = "/pharmacist/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPPLIER"))) {
            redirectUrl = "/supplier/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_FINANCE"))) {
            redirectUrl = "/finance/dashboard";
        }

        // Αν δεν βρεθεί ρόλος, redirect στην αρχική
        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        // Εκτέλεση του redirect στο τελικό url
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
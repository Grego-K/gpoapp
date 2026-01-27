package gr.aueb.cf.gpoapp.authentication;

import gr.aueb.cf.gpoapp.core.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Ενεργοποιεί το @PreAuthorize στους controllers
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthFilter; // Προσθήκη του JWT φίλτρου

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Δημόσια urls και στατικά αρχεία
                        .requestMatchers("/", "/landing", "/login", "/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()

                        // Swagger & API documentation (δημόσια για testing)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Endpoints για το rest API
                        .requestMatchers("/api/auth/**").permitAll() // Για το login που θα επιστρέφει token
                        .requestMatchers("/api/products/**").hasAnyRole("PHARMACIST", "ADMIN")

                        // Περιορισμοί βάσει capabilities - authorities
                        .requestMatchers("/admin/products/edit/**").hasAuthority("CAN_EDIT_PRODUCTS")
                        .requestMatchers("/admin/products/delete/**").hasAuthority("CAN_DELETE_PRODUCTS")
                        .requestMatchers("/admin/orders/edit/**").hasAuthority("CAN_EDIT_ORDERS")
                        .requestMatchers("/admin/orders/delete/**").hasAuthority("CAN_DELETE_ORDERS")

                        // Περιορισμοί βάσει roles
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/pharmacist/**").hasAnyRole("PHARMACIST", "ADMIN")
                        .requestMatchers("/supplier/**").hasRole("SUPPLIER")
                        .requestMatchers("/finance/**").hasRole("FINANCE")

                        // Οτιδήποτε άλλο απαιτεί authentication
                        .anyRequest().authenticated()
                )
                // Ρύθμιση Session: Για τα web Views παραμένει IF_REQUIRED (default)
                // αλλά για το API θα ελέγχεται από το Token
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // Προσθήκη του JWT Filter πριν από τον τυπικό έλεγχο
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/error"));

        return http.build();
    }

    // Προσθήκη AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Προσθήκη PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
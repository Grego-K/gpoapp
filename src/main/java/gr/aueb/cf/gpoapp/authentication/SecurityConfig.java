package gr.aueb.cf.gpoapp.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Ενεργοποιεί το @PreAuthorize στους controllers
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Δημόσια urls και στατικά αρχεία
                        .requestMatchers("/", "/landing", "/login", "/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()

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
}
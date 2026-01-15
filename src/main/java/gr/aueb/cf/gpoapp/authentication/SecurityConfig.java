package gr.aueb.cf.gpoapp.authentication;

import gr.aueb.cf.gpoapp.authentication.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Δημόσια URLs και web accessed files
                        .requestMatchers("/", "/landing", "/login", "/register").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()

                        // Περιορισμοί βάσει authorization role (pharmacist/supplier/admin)
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/pharmacist/**").hasAuthority("PHARMACIST")
                        .requestMatchers("/supplier/**").hasAuthority("SUPPLIER")

                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        // Χρήση του custom success handler για το redirect
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                // Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redirect στην αρχική
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
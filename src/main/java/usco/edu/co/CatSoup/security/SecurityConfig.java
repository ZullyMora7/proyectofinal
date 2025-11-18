package usco.edu.co.CatSoup.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 🚫 Desactivamos CSRF
                .csrf(csrf -> csrf.disable())

                // 📌 AUTORIZACIÓN DE RUTAS
                .authorizeHttpRequests(auth -> auth
                        // Públicas
                        .requestMatchers("/", "/home", "/login", "/register", "/redirect",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        // Admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // User y admin
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        // Todo lo demás: requiere autenticación
                        .anyRequest().authenticated()
                )

                // 🔑 LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirect", true)
                        .permitAll()
                )

                // 🔧 LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


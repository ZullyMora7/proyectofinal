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
                // ✅ Evita errores con formularios POST
                .csrf(csrf -> csrf.disable())

                // ✅ AUTORIZACIÓN DE RUTAS
                .authorizeHttpRequests(auth -> auth
                        // ✅ Rutas públicas
                        .requestMatchers(
                                "/", "/home", "/login", "/register", "/redirect",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()

                        // ✅ Rutas protegidas
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 🔹 Permitir que tanto USER como ADMIN accedan a rutas de usuario
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        // ✅ Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )

                // ✅ LOGIN PERSONALIZADO
                .formLogin(form -> form
                        .loginPage("/login")
                        // Después del login, Spring va a /redirect para saber si eres admin o user
                        .defaultSuccessUrl("/redirect", true)
                        .permitAll()
                )

                // ✅ LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // ✅ AUTENTICACIÓN
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

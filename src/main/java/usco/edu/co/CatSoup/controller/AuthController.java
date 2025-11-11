package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/")
    public String root() {
        // Página pública inicial
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication auth) {

        // ✅ Verificamos si la autenticación no es nula (seguridad)
        if (auth == null) {
            return "redirect:/login";
        }

        // ✅ Redirigir según el rol del usuario
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/home";
            } else if (role.equals("ROLE_USER")) {
                return "redirect:/user/home";
            }
        }

        // ✅ Si no tiene rol válido, lo mandamos al login
        return "redirect:/login?error=rol_no_valido";
    }
}


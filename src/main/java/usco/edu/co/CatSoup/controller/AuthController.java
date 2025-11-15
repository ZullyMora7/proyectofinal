package usco.edu.co.CatSoup.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        if (auth == null) {
            return "redirect:/login";
        }

        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/home";
            } else if (role.equals("ROLE_USER")) {
                return "redirect:/user/home";
            }
        }

        return "redirect:/login?error=rol_no_valido";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 🔥 Cierra la sesión REALMENTE
        }
        return "redirect:/login?logout";
    }
}


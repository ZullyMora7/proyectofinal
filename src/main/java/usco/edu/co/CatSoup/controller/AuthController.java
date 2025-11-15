package usco.edu.co.CatSoup.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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

    // ✅ POST /register corregido — con validación
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, RedirectAttributes redirect) {

        try {
            userService.registerUser(user);
            redirect.addFlashAttribute("success", "Usuario registrado exitosamente");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {

            if (e.getMessage().equals("EMAIL_EXISTS")) {
                redirect.addFlashAttribute("error", "El correo ya existe");
            }

            if (e.getMessage().equals("USERNAME_EXISTS")) {
                redirect.addFlashAttribute("error", "El username ya existe");
            }

            return "redirect:/register";
        }
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


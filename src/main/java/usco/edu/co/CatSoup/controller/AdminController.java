package usco.edu.co.CatSoup.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================================
    // HOME ADMIN
    // ================================
    @GetMapping("/home")
    public String adminHome() {
        return "admin/home";
    }

    // ================================
    // PERFIL ADMIN (GET)
    // ================================
    @GetMapping("/perfil")
    public String mostrarPerfilAdmin(Model model, RedirectAttributes redirectAttrs) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            redirectAttrs.addFlashAttribute("error", "Debe iniciar sesión para ver su perfil.");
            return "redirect:/login";
        }

        String loginName = auth.getName();
        User admin = userService.findByEmail(loginName).orElse(null);

        if (admin == null) {
            admin = userService.findByUsername(loginName).orElse(null);
        }

        if (admin == null) {
            redirectAttrs.addFlashAttribute("error", "Administrador no encontrado.");
            return "redirect:/login";
        }

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", admin);
        }

        return "admin/perfil";
    }

    // ================================
    // PERFIL ADMIN (POST)
    // ================================
    @PostMapping("/perfil")
    public String actualizarPerfilAdmin(@ModelAttribute("user") User formUser,
                                        HttpServletRequest request,
                                        RedirectAttributes redirectAttrs) {

        if (formUser.getId() == null) {
            redirectAttrs.addFlashAttribute("error", "ID inválido. Intente nuevamente.");
            return "redirect:/admin/perfil";
        }

        User current = userService.findById(formUser.getId()).orElse(null);

        if (current == null) {
            redirectAttrs.addFlashAttribute("error", "Administrador no encontrado.");
            return "redirect:/login";
        }

        // Validación campos obligatorios
        if (formUser.getUsername() == null || formUser.getUsername().trim().isEmpty() ||
            formUser.getEmail() == null || formUser.getEmail().trim().isEmpty()) {

            redirectAttrs.addFlashAttribute("error", "Debe llenar todos los campos obligatorios.");
            return "redirect:/admin/perfil";
        }

        String newEmail = formUser.getEmail().trim();
        String newUsername = formUser.getUsername().trim();

        // Email único
        if (!newEmail.equalsIgnoreCase(current.getEmail())) {
            if (userService.findByEmail(newEmail).isPresent()) {
                redirectAttrs.addFlashAttribute("error", "El correo ya está en uso.");
                return "redirect:/admin/perfil";
            }
        }

        // Username único
        if (!newUsername.equalsIgnoreCase(current.getUsername())) {
            if (userService.findByUsername(newUsername).isPresent()) {
                redirectAttrs.addFlashAttribute("error", "El nombre de usuario ya está en uso.");
                return "redirect:/admin/perfil";
            }
        }

        // Actualizar datos
        current.setUsername(newUsername);
        current.setEmail(newEmail);

        // Actualizar contraseña si cambia
        if (formUser.getPassword() != null && !formUser.getPassword().trim().isEmpty()) {
            String newPass = formUser.getPassword().trim();

            if (!passwordEncoder.matches(newPass, current.getPassword())) {
                current.setPassword(passwordEncoder.encode(newPass));
            }
        }

        userService.save(current);

        // Cerrar sesión por seguridad
        try {
            if (request.getSession(false) != null) {
                request.getSession(false).invalidate();
            }
        } catch (Exception ignored) {}

        SecurityContextHolder.clearContext();

        redirectAttrs.addFlashAttribute("success",
                "Perfil actualizado correctamente. Por seguridad, inicia sesión de nuevo.");

        return "redirect:/login";
    }
}

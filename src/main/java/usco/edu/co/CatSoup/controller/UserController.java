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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/home")
    public String userHome() {
        return "user/home";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, RedirectAttributes redirectAttrs) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            redirectAttrs.addFlashAttribute("error", "Debe iniciar sesión para ver su perfil.");
            return "redirect:/login";
        }

        String loginName = auth.getName();

        User user = userService.findByEmail(loginName).orElse(null);

        if (user == null) {
            user = userService.findByUsername(loginName).orElse(null);
        }

        if (user == null) {
            redirectAttrs.addFlashAttribute("error", "Usuario no encontrado. Vuelva a iniciar sesión.");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user/perfil";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@ModelAttribute("user") User formUser,
                                   Model model,
                                   HttpServletRequest request,
                                   RedirectAttributes redirectAttrs) {

        if (formUser.getId() == null) {
            redirectAttrs.addFlashAttribute("error", "ID de usuario inválido. Intente nuevamente.");
            return "redirect:/user/perfil";
        }

        User current = userService.findById(formUser.getId()).orElse(null);

        if (current == null) {
            redirectAttrs.addFlashAttribute("error", "Usuario no encontrado en base de datos.");
            return "redirect:/login";
        }

        // Campos obligatorios
        if (formUser.getUsername() == null || formUser.getUsername().trim().isEmpty() ||
            formUser.getEmail() == null || formUser.getEmail().trim().isEmpty() ||
            formUser.getPassword() == null || formUser.getPassword().trim().isEmpty()) {

            redirectAttrs.addFlashAttribute("error", "Debe llenar todos los campos.");
            return "redirect:/user/perfil";
        }

        String newEmail = formUser.getEmail().trim();
        String newUsername = formUser.getUsername().trim();

        // VALIDAR EMAIL ÚNICO (arreglado)
        if (!newEmail.equalsIgnoreCase(current.getEmail())) {
            userService.findByEmail(newEmail).ifPresent(other -> {
                if (!other.getId().equals(current.getId())) {
                    redirectAttrs.addFlashAttribute("error", "El correo ya está en uso.");
                }
            });

            if (redirectAttrs.containsAttribute("error")) {
                return "redirect:/user/perfil";
            }
        }

        // VALIDAR USERNAME ÚNICO (arreglado)
        if (!newUsername.equalsIgnoreCase(current.getUsername())) {
            userService.findByUsername(newUsername).ifPresent(other -> {
                if (!other.getId().equals(current.getId())) {
                    redirectAttrs.addFlashAttribute("error", "El nombre de usuario ya está en uso.");
                }
            });

            if (redirectAttrs.containsAttribute("error")) {
                return "redirect:/user/perfil";
            }
        }

        // Guardar cambios
        current.setUsername(newUsername);
        current.setEmail(newEmail);
        current.setPassword(passwordEncoder.encode(formUser.getPassword()));

        userService.save(current);

        // Cerrar sesión
        try {
            request.getSession(false).invalidate();
        } catch (Exception ignored) {}

        SecurityContextHolder.clearContext();

        redirectAttrs.addFlashAttribute("success",
                "Perfil actualizado correctamente. Por seguridad, inicia sesión de nuevo.");

        return "redirect:/login";
    }
}

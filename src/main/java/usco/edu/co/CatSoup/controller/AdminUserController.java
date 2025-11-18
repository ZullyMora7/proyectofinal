package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Role;
import usco.edu.co.CatSoup.service.UserService;
import usco.edu.co.CatSoup.repository.RoleRepository;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    // ============================================================
    // LISTAR USUARIOS
    // ============================================================
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/users";
    }

    // ============================================================
    // FORMULARIO: CREAR USUARIO
    // ============================================================
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/users/new-user";
    }

    // ============================================================
    // GUARDAR USUARIO
    // ============================================================
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam("roleId") Long roleId,
                           Model model) {

        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            user.setRole(role);

            userService.registerUser(user);

        } catch (IllegalArgumentException e) {

            if ("EMAIL_EXISTS".equals(e.getMessage())) {
                model.addAttribute("error", "El correo electrónico ya está registrado ⚠️");
            } else if ("USERNAME_EXISTS".equals(e.getMessage())) {
                model.addAttribute("error", "El nombre de usuario ya está registrado ⚠️");
            } else {
                model.addAttribute("error", "Error inesperado: " + e.getMessage());
            }

            model.addAttribute("user", user);
            model.addAttribute("roles", roleRepository.findAll());

            return "admin/users/new-user";
        }

        return "redirect:/admin/users?created=true";
    }

    // ============================================================
    // FORMULARIO: EDITAR USUARIO
    // ============================================================
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {

        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());

        return "admin/users/edit-user";
    }

    // ============================================================
    // ACTUALIZAR USUARIO
    // ============================================================
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("roleId") Long roleId,
                             Model model) {

        try {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            user.setRole(role);

            userService.updateUser(user);

        } catch (IllegalArgumentException e) {

            if ("EMAIL_EXISTS".equals(e.getMessage())) {
                model.addAttribute("error", "El correo electrónico ya está registrado ⚠️");
            } else if ("USERNAME_EXISTS".equals(e.getMessage())) {
                model.addAttribute("error", "El nombre de usuario ya está registrado ⚠️");
            } else {
                model.addAttribute("error", "Ocurrió un error: " + e.getMessage());
            }

            model.addAttribute("user", user);
            model.addAttribute("roles", roleRepository.findAll());

            return "admin/users/edit-user";
        }

        model.addAttribute("success", "Usuario actualizado correctamente ✔️");

        return "redirect:/admin/users?updated=true";
    }

    // ============================================================
    // ELIMINAR USUARIO
    // ============================================================
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users?deleted=true";
    }
}

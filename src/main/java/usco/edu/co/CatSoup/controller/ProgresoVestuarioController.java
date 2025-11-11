package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Vestuario;
import usco.edu.co.CatSoup.model.UsuarioVestuario;
import usco.edu.co.CatSoup.repository.VestuarioRepository;
import usco.edu.co.CatSoup.service.UsuarioVestuarioService;
import usco.edu.co.CatSoup.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/progreso/vestuario")
public class ProgresoVestuarioController {

    private final VestuarioRepository vestuarioRepository;
    private final UsuarioVestuarioService usuarioVestuarioService;
    private final UserService userService;

    // ==========================================================
    // 🔹 VISTA PRINCIPAL: muestra todos los vestuarios + obtenidos
    // ==========================================================
    @GetMapping
    public String vistaVestuarios(Model model, Authentication auth) {

        if (auth == null || auth.getName() == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();

        List<Vestuario> todos = vestuarioRepository.findAll();
        List<UsuarioVestuario> obtenidos = usuarioVestuarioService.findByUser(user);

        // ✅ Solo enviamos los IDs de los vestuarios obtenidos (más limpio para el HTML)
        List<Long> idsObtenidos = obtenidos.stream()
                .map(uv -> uv.getVestuario().getId())
                .collect(Collectors.toList());

        model.addAttribute("vestuarios", todos);
        model.addAttribute("idsObtenidos", idsObtenidos);
        model.addAttribute("usuario", user);

        return "user/vestuario";
    }

    // ==========================================================
    // 🔹 ACTUALIZA EL PROGRESO (AJAX)
    // ==========================================================
    @PostMapping("/toggle")
    @ResponseBody
    public String toggleVestuario(@RequestParam Long vestuarioId,
                                  @RequestParam boolean obtenido,
                                  Authentication auth) {

        if (auth == null || auth.getName() == null) {
            return "unauthorized";
        }

        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "unauthorized";
        }

        User user = optionalUser.get();

        Optional<Vestuario> optionalVestuario = vestuarioRepository.findById(vestuarioId);
        if (optionalVestuario.isEmpty()) {
            return "not_found";
        }

        Vestuario vestuario = optionalVestuario.get();

        if (obtenido) {
            usuarioVestuarioService.marcarObtenido(user, vestuario);
        } else {
            usuarioVestuarioService.desmarcarObtenido(user, vestuario);
        }

        return "ok";
    }
}


package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import usco.edu.co.CatSoup.model.Gato;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.UsuarioGato;
import usco.edu.co.CatSoup.repository.GatoRepository;
import usco.edu.co.CatSoup.service.UsuarioGatoService;
import usco.edu.co.CatSoup.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/progreso/gatos")
public class ProgresoGatoController {

    private final GatoRepository gatoRepository;
    private final UsuarioGatoService usuarioGatoService;
    private final UserService userService;

    // ==========================================================
    // 🔹 VISTA PRINCIPAL: muestra todos los gatos + los obtenidos
    // ==========================================================
    @GetMapping
    public String vistaGatos(Model model, Authentication auth) {

        // ✅ Verificación de sesión
        if (auth == null || auth.getName() == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();

        // ✅ Listas de gatos
        List<Gato> todos = gatoRepository.findAll();
        List<UsuarioGato> obtenidos = usuarioGatoService.findByUser(user);

        // ✅ NUEVO: conjunto de IDs de gatos obtenidos (para el th:checked)
        Set<Long> idsObtenidos = obtenidos.stream()
                .map(ug -> ug.getGato() != null ? ug.getGato().getId() : null)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        // ✅ Enviar todo al modelo
        model.addAttribute("gatos", todos);
        model.addAttribute("obtenidos", obtenidos); // lo dejas por compatibilidad
        model.addAttribute("idsObtenidos", idsObtenidos); // para el HTML
        model.addAttribute("usuario", user);

        return "user/gatos"; // → Asegúrate de tener templates/user/gatos.html
    }

    // ==========================================================
    // 🔹 ACTUALIZA EL PROGRESO (AJAX)
    // ==========================================================
    @PostMapping("/toggle")
    @ResponseBody
    public String toggleGato(@RequestParam Long gatoId,
                             @RequestParam boolean obtenido,
                             Authentication auth) {

        // ✅ Validar sesión
        if (auth == null || auth.getName() == null) {
            return "unauthorized";
        }

        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "unauthorized";
        }

        User user = optionalUser.get();

        Optional<Gato> optionalGato = gatoRepository.findById(gatoId);
        if (optionalGato.isEmpty()) {
            return "not_found";
        }

        Gato gato = optionalGato.get();

        // ✅ Marcar o desmarcar según el estado
        if (obtenido) {
            usuarioGatoService.marcarObtenido(user, gato);
        } else {
            usuarioGatoService.desmarcarObtenido(user, gato);
        }

        return "ok";
    }
}

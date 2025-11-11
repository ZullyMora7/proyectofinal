package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import usco.edu.co.CatSoup.model.Estacion;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.UsuarioEstacion;
import usco.edu.co.CatSoup.repository.EstacionRepository;
import usco.edu.co.CatSoup.service.UsuarioEstacionService;
import usco.edu.co.CatSoup.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/progreso/estaciones")
public class ProgresoEstacionController {

    private final EstacionRepository estacionRepository;
    private final UsuarioEstacionService usuarioEstacionService;
    private final UserService userService;

    // ==========================================================
    // 🔹 VISTA PRINCIPAL: muestra todas las estaciones + obtenidas
    // ==========================================================
    @GetMapping
    public String vistaEstaciones(Model model, Authentication auth) {

        // ✅ Verificación de sesión
        if (auth == null || auth.getName() == null) {
            return "redirect:/login";
        }

        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }

        User user = optionalUser.get();

        // ✅ Listas de estaciones
        List<Estacion> todas = estacionRepository.findAll();
        List<UsuarioEstacion> obtenidas = usuarioEstacionService.findByUser(user);

        // ✅ Conjunto de IDs de estaciones obtenidas
        Set<Long> idsObtenidas = obtenidas.stream()
                .map(ue -> ue.getEstacion() != null ? ue.getEstacion().getId() : null)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        // ✅ Enviar al modelo
        model.addAttribute("estaciones", todas);
        model.addAttribute("obtenidas", obtenidas);
        model.addAttribute("idsObtenidas", idsObtenidas);
        model.addAttribute("usuario", user);

        return "user/estaciones";
    }

    // ==========================================================
    // 🔹 ACTUALIZA EL PROGRESO (AJAX)
    // ==========================================================
    @PostMapping("/toggle")
    @ResponseBody
    public String toggleEstacion(@RequestParam Long estacionId,
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

        Optional<Estacion> optionalEstacion = estacionRepository.findById(estacionId);
        if (optionalEstacion.isEmpty()) {
            return "not_found";
        }

        Estacion estacion = optionalEstacion.get();

        // ✅ Marcar o desmarcar
        if (obtenido) {
            usuarioEstacionService.marcarObtenido(user, estacion);
        } else {
            usuarioEstacionService.desmarcarObtenido(user, estacion);
        }

        return "ok";
    }
}


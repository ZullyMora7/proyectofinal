package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.service.ProgresoService;
import usco.edu.co.CatSoup.service.UserService;

@Controller
@RequiredArgsConstructor
public class ProgresoController {

    private final UserService userService;
    private final ProgresoService progresoService;

    @GetMapping("/user/progreso")
    public String progresoUsuario(Model model, Authentication auth) {

        // 🛡️ Seguridad básica
        if (auth == null || auth.getName() == null) {
            return "redirect:/login";
        }

        // 🧑‍💻 Obtener el usuario autenticado
        User user = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + auth.getName()));

        // 📊 Calcular datos del progreso usando tu ProgresoService
        double porcGatos = progresoService.porcentajeGatos(user);
        double porcVestuarios = progresoService.porcentajeVestuarios(user);
        double porcEstaciones = progresoService.porcentajeEstaciones(user);

        int gatosObtenidos = progresoService.gatosObtenidos(user);
        int totalGatos = progresoService.totalGatos();

        int vestuariosObtenidos = progresoService.vestuariosObtenidos(user);
        int totalVestuarios = progresoService.totalVestuarios();

        int estacionesObtenidas = progresoService.estacionesObtenidas(user);
        int totalEstaciones = progresoService.totalEstaciones();

        // 🧩 Pasar los datos a la vista
        model.addAttribute("porGatos", porcGatos);
        model.addAttribute("porVestuarios", porcVestuarios);
        model.addAttribute("porEstaciones", porcEstaciones);

        model.addAttribute("gatosObtenidos", gatosObtenidos);
        model.addAttribute("totalGatos", totalGatos);

        model.addAttribute("vestuariosObtenidos", vestuariosObtenidos);
        model.addAttribute("totalVestuarios", totalVestuarios);

        model.addAttribute("estacionesObtenidas", estacionesObtenidas);
        model.addAttribute("totalEstaciones", totalEstaciones);

        // 🔗 Retornar la vista
        return "user/progreso";
    }
}

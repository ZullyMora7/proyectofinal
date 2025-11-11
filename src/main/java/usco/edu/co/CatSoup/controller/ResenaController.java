package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import usco.edu.co.CatSoup.model.Resena;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.service.ResenaService;
import usco.edu.co.CatSoup.service.UserService;
import usco.edu.co.CatSoup.service.GatoService;
import usco.edu.co.CatSoup.service.EstacionService;  // ✅ IMPORTANTE

@Controller
@RequiredArgsConstructor
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;
    private final UserService userService;
    private final GatoService gatoService;
    private final EstacionService estacionService; // ✅ AGREGADO

    @PostMapping("/agregar")
    public String agregarResena(@RequestParam int calificacion,
                                @RequestParam String comentario,
                                @RequestParam Long objetoId,
                                @RequestParam String tipoObjeto,
                                Authentication auth) {

        User user = userService.findByEmail(auth.getName()).get();

        Resena r = new Resena();
        r.setCalificacion(calificacion);
        r.setComentario(comentario);
        r.setObjetoId(objetoId);
        r.setTipoObjeto(tipoObjeto);
        r.setUsuario(user);

        resenaService.save(r);

        return switch (tipoObjeto) {
            case "gato" -> "redirect:/user/progreso/gatos";
            case "estacion" -> "redirect:/user/progreso/estaciones";
            case "vestuario" -> "redirect:/user/progreso/vestuario";
            default -> "redirect:/";
        };
    }

    // ✅ Ver reseñas de un gato con nombre real
    @GetMapping("/ver/gato/{id}")
    public String verResenasGato(@PathVariable Long id, Model model) {

        var gato = gatoService.findById(id); // ✅ correcto

        model.addAttribute("resenas", resenaService.getResenas("gato", id));
        model.addAttribute("titulo", "Reseñas del gato " + gato.getNombre());

        return "resenas/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarResena(@PathVariable Long id, Authentication auth) {

        Resena r = resenaService.findById(id);

        boolean esDueno = r.getUsuario().getEmail().equals(auth.getName());

        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esDueno && !esAdmin) {
            return "redirect:/error/403";
        }

        resenaService.delete(id);

        return "redirect:/resenas/ver/" + r.getTipoObjeto() + "/" + r.getObjetoId();
    }

    // ✅ Ver reseñas de una estación con nombre real
    @GetMapping("/ver/estacion/{id}")
    public String verResenasEstacion(@PathVariable Long id, Model model) {

        var estacion = estacionService.findById(id); // ✅ YA FUNCIONA

        model.addAttribute("resenas", resenaService.getResenas("estacion", id));
        model.addAttribute("titulo", "Reseñas de la estación " + estacion.getNombre());

        return "resenas/listarEstaciones";
    }
}


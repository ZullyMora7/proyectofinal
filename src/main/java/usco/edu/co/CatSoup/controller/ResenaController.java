package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import usco.edu.co.CatSoup.model.Resena;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Vestuario;
import usco.edu.co.CatSoup.service.ResenaService;
import usco.edu.co.CatSoup.service.UserService;
import usco.edu.co.CatSoup.service.GatoService;
import usco.edu.co.CatSoup.service.EstacionService;
import usco.edu.co.CatSoup.service.VestuarioService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;
    private final UserService userService;
    private final GatoService gatoService;
    private final EstacionService estacionService;
    private final VestuarioService vestuarioService;

    @PostMapping("/agregar")
    public String agregarResena(
            @RequestParam(name = "calificacion") int calificacion,
            @RequestParam(name = "comentario") String comentario,
            @RequestParam(name = "objetoId") Long objetoId,
            @RequestParam(name = "tipoObjeto") String tipoObjeto,
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

    @GetMapping("/ver/gato/{id}")
    public String verResenasGato(@PathVariable Long id, Model model) {

        var gato = gatoService.findById(id);

        model.addAttribute("resenas", resenaService.getResenas("gato", id));
        model.addAttribute("titulo", "Reseñas del gato \"" + gato.getNombre() +"\"");

        return "resenas/listar";
    }

    @GetMapping("/ver/estacion/{id}")
    public String verResenasEstacion(@PathVariable Long id, Model model) {

        var estacion = estacionService.findById(id);

        model.addAttribute("resenas", resenaService.getResenas("estacion", id));
        model.addAttribute("titulo", "Reseñas de la estación de cocina \"" + estacion.getNombre() + "\"");

        return "resenas/listar";
    }

    @GetMapping("/ver/vestuario/{id}")
    public String verResenasVestuario(@PathVariable Long id, Model model) {

        Vestuario vestuario = vestuarioService.findById(id);
        model.addAttribute("vestuario", vestuario);

        model.addAttribute("resenas",
                resenaService.getResenas("vestuario", id));

        model.addAttribute("titulo",
                "Reseñas del vestuario \"" + vestuario.getNombre() + "\"");

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

}

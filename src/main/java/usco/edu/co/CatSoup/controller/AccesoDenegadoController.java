package usco.edu.co.CatSoup.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccesoDenegadoController {

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Authentication auth, Model model) {

        String mensaje;

        if (auth != null) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                mensaje = "Esta vista está restringida solo para USUARIOS";
            } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                mensaje = "Esta vista está restringida solo para ADMINISTRADORES";
            } else {
                mensaje = "No tienes permisos para acceder a esta página.";
            }
        } else {
            mensaje = "Debes iniciar sesión para acceder.";
        }

        model.addAttribute("mensaje", mensaje);
        return "acceso-denegado";
    }
}

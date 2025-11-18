package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usco.edu.co.CatSoup.model.Publicacion;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.service.PublicacionService;
import usco.edu.co.CatSoup.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final UserService userService;

    // =====================================================
    // 🔹 RUTAS DE USUARIO NORMAL
    // =====================================================
    @Controller
    @RequestMapping("/user/publicaciones")
    @RequiredArgsConstructor
    static class UserPublicacionController {

        private final PublicacionService publicacionService;
        private final UserService userService;

        private static final String UPLOAD_DIR = "uploads/publicaciones/";

        private String getRedirectByRole(Authentication auth) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            return isAdmin ? "redirect:/admin/publicaciones" : "redirect:/user/publicaciones";
        }

        @GetMapping
        public String listar(Model model, Authentication auth) {
            model.addAttribute("publicaciones", publicacionService.findAll());
            User user = userService.findByEmail(auth.getName()).get();
            model.addAttribute("userId", user.getId());
            return "user/publicaciones";
        }

        @GetMapping("/crear")
        public String mostrarFormulario(Model model) {
            model.addAttribute("publicacion", new Publicacion());
            return "fragments/form-publicaciones";
        }

        @PostMapping("/crear")
        public String crear(@RequestParam String titulo,
                            @RequestParam String contenido,
                            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                            Authentication auth) throws IOException {

            User user = userService.findByEmail(auth.getName()).get();
            Publicacion p = new Publicacion();
            p.setTitulo(titulo);
            p.setContenido(contenido);
            p.setUser(user);

            if (imagen != null && !imagen.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, imagen.getBytes());

                p.setImagen("/uploads/publicaciones/" + fileName);
            }

            publicacionService.save(p);
            return getRedirectByRole(auth);
        }

        @GetMapping("/eliminar/{id}")
        public String eliminar(@PathVariable Long id, Authentication auth) {
            User user = userService.findByEmail(auth.getName()).get();
            Publicacion p = publicacionService.findById(id);

            if (p.getUser().getId().equals(user.getId())) {
                publicacionService.delete(id);
            }
            return getRedirectByRole(auth);
        }

        // 🔄 Nueva ruta: editar publicación
        @GetMapping("/editar/{id}")
        public String editar(@PathVariable Long id, Model model, Authentication auth) {
            Publicacion p = publicacionService.findById(id);
            User user = userService.findByEmail(auth.getName()).get();

            if (p == null || !p.getUser().getId().equals(user.getId())) {
                return "redirect:/user/publicaciones";
            }

            model.addAttribute("publicacion", p);
            return "fragments/form-publicaciones";
        }

        @PostMapping("/editar")
        public String actualizar(@ModelAttribute Publicacion publicacion,
                                 @RequestParam("imagen") MultipartFile imagen,
                                 Authentication auth) throws IOException {

            User user = userService.findByEmail(auth.getName()).get();
            Publicacion p = publicacionService.findById(publicacion.getId());

            if (p == null || !p.getUser().getId().equals(user.getId())) {
                return "redirect:/user/publicaciones";
            }

            p.setTitulo(publicacion.getTitulo());
            p.setContenido(publicacion.getContenido());

            if (!imagen.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, imagen.getBytes());

                p.setImagen("/uploads/publicaciones/" + fileName);
            }

            publicacionService.save(p);
            return "redirect:/user/publicaciones";
        }
    }

    // =====================================================
    // 🔹 RUTAS DE ADMINISTRADOR
    // =====================================================
    @Controller
    @RequestMapping("/admin/publicaciones")
    @RequiredArgsConstructor
    static class AdminPublicacionController {

        private final PublicacionService publicacionService;
        private final UserService userService;

        private static final String UPLOAD_DIR = "uploads/publicaciones/";

        @GetMapping
        public String listar(Model model) {
            model.addAttribute("publicaciones", publicacionService.findAll());
            return "admin/publicaciones";
        }

        @GetMapping("/crear")
        public String mostrarFormulario(Model model) {
            model.addAttribute("publicacion", new Publicacion());
            return "fragments/form-publicaciones";
        }

        @PostMapping("/crear")
        public String crear(@RequestParam String titulo,
                            @RequestParam String contenido,
                            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                            Authentication auth) throws IOException {

            User user = userService.findByEmail(auth.getName()).get();
            Publicacion p = new Publicacion();
            p.setTitulo(titulo);
            p.setContenido(contenido);
            p.setUser(user);

            if (imagen != null && !imagen.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, imagen.getBytes());

                p.setImagen("/uploads/publicaciones/" + fileName);
            }

            publicacionService.save(p);
            return "redirect:/admin/publicaciones";
        }

        @GetMapping("/eliminar/{id}")
        public String eliminar(@PathVariable Long id) {
            publicacionService.delete(id);
            return "redirect:/admin/publicaciones";
        }

        // 🔄 Nueva ruta: editar publicación como admin
        @GetMapping("/editar/{id}")
        public String editar(@PathVariable Long id, Model model) {
            Publicacion p = publicacionService.findById(id);

            if (p == null) {
                return "redirect:/admin/publicaciones";
            }

            model.addAttribute("publicacion", p);
            return "fragments/form-publicaciones";
        }

        @PostMapping("/editar")
        public String actualizar(@ModelAttribute Publicacion publicacion,
                                 @RequestParam("imagen") MultipartFile imagen) throws IOException {

            Publicacion p = publicacionService.findById(publicacion.getId());

            if (p == null) {
                return "redirect:/admin/publicaciones";
            }

            p.setTitulo(publicacion.getTitulo());
            p.setContenido(publicacion.getContenido());

            if (!imagen.isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, imagen.getBytes());

                p.setImagen("/uploads/publicaciones/" + fileName);
            }

            publicacionService.save(p);
            return "redirect:/admin/publicaciones";
        }
    }
}


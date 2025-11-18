package usco.edu.co.CatSoup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usco.edu.co.CatSoup.model.Gato;
import usco.edu.co.CatSoup.service.GatoService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/gatos")
public class AdminGatoController {

    private final GatoService gatoService;

    // LISTAR
    @GetMapping
    public String listarGatos(Model model) {
        model.addAttribute("gatos", gatoService.findAll());
        return "admin/gatos/listar";
    }

    // FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearGato(Model model) {
        model.addAttribute("gato", new Gato());
        return "admin/gatos/crear";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Gato gato,
                          BindingResult result,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          Model model) throws IOException {

        if (result.hasErrors()) {
            return "admin/gatos/crear";
        }

        if (gatoService.existeNombre(gato.getNombre())) {
            model.addAttribute("error", "El nombre ya existe.");
            return "admin/gatos/crear";
        }

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String nombreArchivoOriginal = imagenArchivo.getOriginalFilename();

            if (gatoService.existeImagen("/uploads/gatos/" + nombreArchivoOriginal)) {
                model.addAttribute("error", "Ya existe un gato con esa imagen.");
                return "admin/gatos/crear";
            }
        }

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String rutaUploads = "uploads/gatos/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + imagenArchivo.getOriginalFilename();
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            gato.setImagen("/uploads/gatos/" + nombreArchivo);
        }

        gatoService.save(gato);
        return "redirect:/admin/gatos";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("gato", gatoService.findById(id));
        return "admin/gatos/editar";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute Gato gatoActualizado,
                             BindingResult result,
                             @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                             Model model) throws IOException {

        Gato gato = gatoService.findById(id);

        // ERRORES DE VALIDACIÓN
        if (result.hasErrors()) {
            model.addAttribute("gato", gatoActualizado);  // ← FIX
            return "admin/gatos/editar";
        }

        // NOMBRE REPETIDO
        if (!gato.getNombre().equals(gatoActualizado.getNombre()) &&
                gatoService.existeNombre(gatoActualizado.getNombre())) {

            model.addAttribute("error", "El nombre ya existe.");
            model.addAttribute("gato", gatoActualizado);  // ← FIX
            return "admin/gatos/editar";
        }

        // APLICAR CAMBIOS
        gato.setNombre(gatoActualizado.getNombre());
        gato.setDescripcion(gatoActualizado.getDescripcion());

        // VALIDACIÓN imagen repetida
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String nombreOriginal = imagenArchivo.getOriginalFilename();

            if (gatoService.existeImagen("/uploads/gatos/" + nombreOriginal)) {
                model.addAttribute("error", "Ya existe un gato con esa imagen.");
                model.addAttribute("gato", gatoActualizado);  // ← FIX
                return "admin/gatos/editar";
            }

            String rutaUploads = "uploads/gatos/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + nombreOriginal;
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            gato.setImagen("/uploads/gatos/" + nombreArchivo);
        }

        gatoService.save(gato);
        return "redirect:/admin/gatos";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        gatoService.delete(id);
        return "redirect:/admin/gatos";
    }
}


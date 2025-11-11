package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // ✅ LISTAR GATOS
    @GetMapping
    public String listarGatos(Model model) {
        model.addAttribute("gatos", gatoService.findAll());
        return "admin/gatos/listar";
    }

    // ✅ FORMULARIO CREAR
    @GetMapping("/crear")
    public String crearGato(Model model) {
        model.addAttribute("gato", new Gato());
        return "admin/gatos/crear";
    }

    // ✅ GUARDAR NUEVO GATO CON IMAGEN
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Gato gato,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) throws IOException {

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

    // ✅ FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("gato", gatoService.findById(id));
        return "admin/gatos/editar";
    }

    // ✅ ACTUALIZAR GATO CON IMAGEN NUEVA (si se sube)
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Gato gatoActualizado,
                             @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) throws IOException {

        Gato gato = gatoService.findById(id);

        gato.setNombre(gatoActualizado.getNombre());
        gato.setDescripcion(gatoActualizado.getDescripcion());

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

    // ✅ ELIMINAR GATO (simple y funcional)
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        gatoService.delete(id);
        return "redirect:/admin/gatos";
    }
}


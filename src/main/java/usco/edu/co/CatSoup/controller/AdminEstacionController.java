package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usco.edu.co.CatSoup.model.Estacion;
import usco.edu.co.CatSoup.service.EstacionService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/estaciones")
public class AdminEstacionController {

    private final EstacionService estacionService;

    // ✅ LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estaciones", estacionService.findAll());
        return "admin/estaciones/listar";
    }

    // ✅ CREAR
    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("estacion", new Estacion());
        return "admin/estaciones/crear";
    }

    // ✅ GUARDAR NUEVA ESTACIÓN CON IMAGEN
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Estacion estacion,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) throws IOException {

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String rutaUploads = "uploads/estaciones/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + imagenArchivo.getOriginalFilename();
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            estacion.setImagen("/uploads/estaciones/" + nombreArchivo);
        }

        estacionService.save(estacion);
        return "redirect:/admin/estaciones";
    }

    // ✅ EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("estacion", estacionService.findById(id));
        return "admin/estaciones/editar";
    }

    // ✅ ACTUALIZAR ESTACIÓN (mantiene imagen si no se sube una nueva)
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Estacion estacionForm,
                             @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) throws IOException {

        Estacion estacion = estacionService.findById(id);

        estacion.setNombre(estacionForm.getNombre());
        estacion.setDescripcion(estacionForm.getDescripcion());

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String rutaUploads = "uploads/estaciones/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + imagenArchivo.getOriginalFilename();
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            estacion.setImagen("/uploads/estaciones/" + nombreArchivo);
        }

        estacionService.save(estacion);
        return "redirect:/admin/estaciones";
    }

    // ✅ ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        estacionService.delete(id);
        return "redirect:/admin/estaciones";
    }
}

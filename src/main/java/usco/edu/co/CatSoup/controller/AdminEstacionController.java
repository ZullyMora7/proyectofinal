package usco.edu.co.CatSoup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estaciones", estacionService.findAll());
        return "admin/estaciones/listar";
    }

    // FORM CREAR
    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("estacion", new Estacion());
        return "admin/estaciones/crear";
    }

    // GUARDAR NUEVA ESTACIÓN
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("estacion") Estacion estacion,
                          BindingResult result,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          Model model) throws IOException {

        // ❗ VALIDACIONES DE ANOTACIONES (@Size, @NotBlank)
        if (result.hasErrors()) {
            return "admin/estaciones/crear";
        }

        // ❗ VALIDAR NOMBRE REPETIDO
        if (estacionService.existeNombre(estacion.getNombre())) {
            model.addAttribute("error", "El nombre ya existe.");
            return "admin/estaciones/crear";
        }

        // ❗ VALIDAR IMAGEN REPETIDA
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            String nombreOriginal = imagenArchivo.getOriginalFilename();

            if (estacionService.existeImagen("/uploads/estaciones/" + nombreOriginal)) {
                model.addAttribute("error", "Ya existe una estación con esa imagen.");
                return "admin/estaciones/crear";
            }
        }

        // SUBIR IMAGEN
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

    // FORM EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("estacion", estacionService.findById(id));
        return "admin/estaciones/editar";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("estacion") Estacion estacionForm,
                             BindingResult result,
                             @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                             Model model) throws IOException {

        Estacion estacion = estacionService.findById(id);

        // ❗ ERRORES DE VALIDACIÓN (nombre, descripción)
        if (result.hasErrors()) {
            return "admin/estaciones/editar";
        }

        // ❗ NOMBRE REPETIDO
        if (!estacion.getNombre().equals(estacionForm.getNombre())
                && estacionService.existeNombre(estacionForm.getNombre())) {

            model.addAttribute("error", "El nombre ya existe.");
            return "admin/estaciones/editar";
        }

        // ACTUALIZAR CAMPOS
        estacion.setNombre(estacionForm.getNombre());
        estacion.setDescripcion(estacionForm.getDescripcion());

        // ❗ VALIDAR IMAGEN REPETIDA
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String nombreOriginal = imagenArchivo.getOriginalFilename();

            if (estacionService.existeImagen("/uploads/estaciones/" + nombreOriginal)) {
                model.addAttribute("error", "Ya existe una estación con esa imagen.");
                return "admin/estaciones/editar";
            }

            // SUBIR NUEVA IMAGEN
            String rutaUploads = "uploads/estaciones/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + nombreOriginal;
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            estacion.setImagen("/uploads/estaciones/" + nombreArchivo);
        }

        estacionService.save(estacion);
        return "redirect:/admin/estaciones";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        estacionService.delete(id);
        return "redirect:/admin/estaciones";
    }
}

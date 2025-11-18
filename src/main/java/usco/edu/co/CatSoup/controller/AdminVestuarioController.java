package usco.edu.co.CatSoup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import usco.edu.co.CatSoup.model.Vestuario;
import usco.edu.co.CatSoup.service.VestuarioService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/vestuario")
public class AdminVestuarioController {

    private final VestuarioService vestuarioService;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vestuarios", vestuarioService.findAll());
        return "admin/vestuario/listar";
    }

    // CREAR
    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("vestuario", new Vestuario());
        return "admin/vestuario/crear";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("vestuario") Vestuario vestuario,
                          BindingResult result,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                          Model model) throws IOException {

        // VALIDACIONES ANOTADAS
        if (result.hasErrors()) {
            return "admin/vestuario/crear";
        }

        // NOMBRE REPETIDO
        if (vestuarioService.existeNombre(vestuario.getNombre())) {
            model.addAttribute("error", "El nombre ya existe.");
            return "admin/vestuario/crear";
        }

        // IMAGEN REPETIDA
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            String nombreOriginal = imagenArchivo.getOriginalFilename();

            if (vestuarioService.existeImagen("/uploads/vestuario/" + nombreOriginal)) {
                model.addAttribute("error", "Ya existe un vestuario con esa imagen.");
                return "admin/vestuario/crear";
            }
        }

        // SUBIR IMAGEN
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String rutaUploads = "uploads/vestuario/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + imagenArchivo.getOriginalFilename();
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            vestuario.setImagen("/uploads/vestuario/" + nombreArchivo);
        }

        vestuarioService.save(vestuario);
        return "redirect:/admin/vestuario";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("vestuario", vestuarioService.findById(id));
        return "admin/vestuario/editar";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("vestuario") Vestuario vestuarioForm,
                             BindingResult result,
                             @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                             Model model) throws IOException {

        Vestuario vestuario = vestuarioService.findById(id);

        // VALIDACIONES POR ANOTACIONES
        if (result.hasErrors()) {
            return "admin/vestuario/editar";
        }

        // NOMBRE REPETIDO
        if (!vestuario.getNombre().equals(vestuarioForm.getNombre())
                && vestuarioService.existeNombre(vestuarioForm.getNombre())) {

            model.addAttribute("error", "El nombre ya existe.");
            return "admin/vestuario/editar";
        }

        // ACTUALIZAR CAMPOS
        vestuario.setNombre(vestuarioForm.getNombre());
        vestuario.setDescripcion(vestuarioForm.getDescripcion());

        // IMAGEN REPETIDA
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {

            String nombreOriginal = imagenArchivo.getOriginalFilename();

            if (vestuarioService.existeImagen("/uploads/vestuario/" + nombreOriginal)) {
                model.addAttribute("error", "Ya existe un vestuario con esa imagen.");
                return "admin/vestuario/editar";
            }

            // SUBIR ARCHIVO
            String rutaUploads = "uploads/vestuario/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            String nombreArchivo = UUID.randomUUID() + "_" + nombreOriginal;
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            vestuario.setImagen("/uploads/vestuario/" + nombreArchivo);
        }

        vestuarioService.save(vestuario);
        return "redirect:/admin/vestuario";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        vestuarioService.delete(id);
        return "redirect:/admin/vestuario";
    }
}


package usco.edu.co.CatSoup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // ✅ LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vestuarios", vestuarioService.findAll());
        return "admin/vestuario/listar";
    }

    // ✅ FORMULARIO CREAR
    @GetMapping("/crear")
    public String crear(Model model) {
        model.addAttribute("vestuario", new Vestuario());
        return "admin/vestuario/crear";
    }

    // ✅ GUARDAR NUEVO VESTUARIO CON IMAGEN
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Vestuario vestuario,
                          @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) throws IOException {

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            String rutaUploads = "uploads/vestuario/";
            File carpeta = new File(rutaUploads);
            if (!carpeta.exists()) carpeta.mkdirs();

            // ✅ Generar nombre único
            String nombreArchivo = UUID.randomUUID() + "_" + imagenArchivo.getOriginalFilename();
            Path rutaArchivo = Paths.get(rutaUploads + nombreArchivo);
            Files.copy(imagenArchivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            // ✅ Guardar la ruta accesible desde el navegador
            vestuario.setImagen("/uploads/vestuario/" + nombreArchivo);
        }

        vestuarioService.save(vestuario);
        return "redirect:/admin/vestuario";
    }

    // ✅ FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("vestuario", vestuarioService.findById(id));
        return "admin/vestuario/editar";
    }

    // ✅ ACTUALIZAR VESTUARIO (si se sube nueva imagen, reemplaza)
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Vestuario vestActualizado,
                             @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) throws IOException {

        Vestuario vestuario = vestuarioService.findById(id);

        vestuario.setNombre(vestActualizado.getNombre());
        vestuario.setTipo(vestActualizado.getTipo());

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

    // ✅ ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        vestuarioService.delete(id);
        return "redirect:/admin/vestuario";
    }
}

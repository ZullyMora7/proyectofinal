package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;   // ✅ AGREGADO
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "gatos")
public class Gato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")   // ✅ VALIDACIÓN
    @Column(length = 50, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")  // ✅ VALIDACIÓN
    @Column(length = 255)
    private String descripcion;

    @Column( unique = true)
    private String imagen;

    @OneToMany(mappedBy = "gato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioGato> usuarioGatos;
}


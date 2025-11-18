package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "vestuarios")
public class Vestuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    @Column(length = 50, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    @Column(unique = true)
    private String imagen;

    // 🔥 RELACIÓN: AL BORRAR UN VESTUARIO SE BORRA TODO LO RELACIONADO
    @OneToMany(mappedBy = "vestuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioVestuario> usuarioVestuarios;
}


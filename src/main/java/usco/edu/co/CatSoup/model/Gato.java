package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List; // ✅ IMPORTANTE: agrega esto

@Entity
@Data
@Table(name = "gatos")
public class Gato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String imagen;
    
    @OneToMany(mappedBy = "gato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioGato> usuarioGatos;

}

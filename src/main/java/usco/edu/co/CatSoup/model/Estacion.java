package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "estaciones")
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    @Column(length = 50, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    @Column( unique = true)
    private String imagen;

    // 🔥 RELACIÓN: AL BORRAR UNA ESTACIÓN SE BORRA TODO LO RELACIONADO
    @OneToMany(mappedBy = "estacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioEstacion> usuarioEstaciones;
}

package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(length = 5000)
    private String contenido;

    private String imagen; // opcional

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    // NUEVOS CAMPOS
    private LocalDateTime fechaEdicion;
    private boolean editado = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

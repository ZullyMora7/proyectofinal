package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_estaciones")
public class UsuarioEstacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "estacion_id")
    private Estacion estacion;

    private boolean obtenido = false;
}

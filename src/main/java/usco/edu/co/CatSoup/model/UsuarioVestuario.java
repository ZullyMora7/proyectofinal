package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_vestuario")
public class UsuarioVestuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vestuario_id")
    private Vestuario vestuario;

    private boolean obtenido = false;
}

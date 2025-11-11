package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_gatos")
public class UsuarioGato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 🔹 Si se elimina un gato, se eliminan también los registros asociados
    @ManyToOne
    @JoinColumn(name = "gato_id", nullable = false)
    private Gato gato;

    private boolean obtenido = false;
}



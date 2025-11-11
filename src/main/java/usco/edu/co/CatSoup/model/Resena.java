package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resenas")
@Getter
@Setter
@NoArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int calificacion; // número de corazones (1 a 5)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario; // usa "User" si tu entidad se llama así

    private Long objetoId; // ID del gato/estación/vestuario
    private String tipoObjeto; // "gato", "estacion", "vestuario"
}

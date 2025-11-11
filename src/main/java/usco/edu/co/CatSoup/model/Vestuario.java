package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "vestuarios")
public class Vestuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String tipo;

    private String imagen;
}

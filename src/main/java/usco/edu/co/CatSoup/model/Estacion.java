package usco.edu.co.CatSoup.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "estaciones")
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String imagen;

}

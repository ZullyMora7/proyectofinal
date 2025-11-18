package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.Gato;

public interface GatoRepository extends JpaRepository<Gato, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByImagen(String imagen);
}

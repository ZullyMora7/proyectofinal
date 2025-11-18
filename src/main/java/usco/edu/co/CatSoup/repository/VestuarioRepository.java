package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.Vestuario;

public interface VestuarioRepository extends JpaRepository<Vestuario, Long> {

    boolean existsByNombre(String nombre);
    boolean existsByImagen(String imagen);
}

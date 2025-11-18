package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.Estacion;

public interface EstacionRepository extends JpaRepository<Estacion, Long> {

    boolean existsByNombre(String nombre);  // ✅ AGREGADO
    
    boolean existsByImagen(String imagen);  // ✅ AGREGADO
}

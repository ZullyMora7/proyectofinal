package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.Publicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}

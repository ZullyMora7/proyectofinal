package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.Resena;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByObjetoIdAndTipoObjeto(Long objetoId, String tipoObjeto);
    List<Resena> findByUsuarioId(Long userId);

   
}

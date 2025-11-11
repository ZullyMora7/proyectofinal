package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.UsuarioGato;
import usco.edu.co.CatSoup.model.Gato;
import usco.edu.co.CatSoup.model.User;

import java.util.List;

public interface UsuarioGatoRepository extends JpaRepository<UsuarioGato, Long> {

    List<UsuarioGato> findByUser(User user);

    boolean existsByUserAndGato(User user, Gato gato);
}

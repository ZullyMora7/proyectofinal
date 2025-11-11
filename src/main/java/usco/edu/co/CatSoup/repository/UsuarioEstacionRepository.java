package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.UsuarioEstacion;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Estacion;

import java.util.List;

public interface UsuarioEstacionRepository extends JpaRepository<UsuarioEstacion, Long> {

    List<UsuarioEstacion> findByUser(User user);

    boolean existsByUserAndEstacion(User user, Estacion estacion);
}

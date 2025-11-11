package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.UsuarioVestuario;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Vestuario;

import java.util.List;

public interface UsuarioVestuarioRepository extends JpaRepository<UsuarioVestuario, Long> {

    List<UsuarioVestuario> findByUser(User user);

    boolean existsByUserAndVestuario(User user, Vestuario vestuario);
}

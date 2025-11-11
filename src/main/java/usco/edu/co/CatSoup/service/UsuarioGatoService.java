package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.Gato;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.UsuarioGato;

import java.util.List;

public interface UsuarioGatoService {

    List<UsuarioGato> findByUser(User user);

    UsuarioGato marcarObtenido(User user, Gato gato);

    UsuarioGato desmarcarObtenido(User user, Gato gato);
}

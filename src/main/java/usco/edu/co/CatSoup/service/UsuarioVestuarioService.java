package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Vestuario;
import usco.edu.co.CatSoup.model.UsuarioVestuario;

import java.util.List;

public interface UsuarioVestuarioService {

    List<UsuarioVestuario> findByUser(User user);

    UsuarioVestuario marcarObtenido(User user, Vestuario vestuario);

    UsuarioVestuario desmarcarObtenido(User user, Vestuario vestuario);
}

package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Estacion;
import usco.edu.co.CatSoup.model.UsuarioEstacion;

import java.util.List;

public interface UsuarioEstacionService {

    List<UsuarioEstacion> findByUser(User user);

    UsuarioEstacion marcarObtenido(User user, Estacion estacion);

    UsuarioEstacion desmarcarObtenido(User user, Estacion estacion);
}



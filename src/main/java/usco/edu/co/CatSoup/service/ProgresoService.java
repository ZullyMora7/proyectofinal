package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.User;

public interface ProgresoService {

    int totalGatos();
    int gatosObtenidos(User user);
    double porcentajeGatos(User user);

    int totalVestuarios();
    int vestuariosObtenidos(User user);
    double porcentajeVestuarios(User user);

    int totalEstaciones();
    int estacionesObtenidas(User user);
    double porcentajeEstaciones(User user);
}

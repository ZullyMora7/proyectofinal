package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.Publicacion;
import usco.edu.co.CatSoup.model.User;

import java.util.List;

public interface PublicacionService {

    List<Publicacion> findAll(); // admin
    List<Publicacion> findByUser(User user); // usuario
    Publicacion save(Publicacion p);
    void delete(Long id);
    Publicacion findById(Long id);
}


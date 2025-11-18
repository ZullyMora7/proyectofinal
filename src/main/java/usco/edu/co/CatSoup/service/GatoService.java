package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.Gato;

import java.util.List;

public interface GatoService {
    List<Gato> findAll();
    Gato findById(Long id);
    Gato save(Gato gato);
    void delete(Long id);

    boolean existeNombre(String nombre);
    boolean existeImagen(String imagen);
}

package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.Vestuario;

import java.util.List;

public interface VestuarioService {

    List<Vestuario> findAll();
    Vestuario findById(Long id);
    Vestuario save(Vestuario vestuario);
    void delete(Long id);

    boolean existeNombre(String nombre);  
    boolean existeImagen(String imagen);  
}

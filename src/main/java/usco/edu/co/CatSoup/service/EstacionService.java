package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.Estacion;

import java.util.List;

public interface EstacionService {

    List<Estacion> findAll();
    Estacion findById(Long id);
    Estacion save(Estacion estacion);
    void delete(Long id);
}

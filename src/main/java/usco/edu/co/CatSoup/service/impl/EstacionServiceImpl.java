package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Estacion;
import usco.edu.co.CatSoup.repository.EstacionRepository;
import usco.edu.co.CatSoup.service.EstacionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacionServiceImpl implements EstacionService {

    private final EstacionRepository estacionRepository;

    @Override
    public List<Estacion> findAll() {
        return estacionRepository.findAll();
    }

    @Override
    public Estacion findById(Long id) {
        return estacionRepository.findById(id).orElse(null);
    }

    @Override
    public Estacion save(Estacion estacion) {
        return estacionRepository.save(estacion);
    }

    @Override
    public void delete(Long id) {
        estacionRepository.deleteById(id);
    }

    @Override
    public boolean existeNombre(String nombre) {   // ✅ AGREGADO
        return estacionRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existeImagen(String imagen) {   // ✅ AGREGADO
        return estacionRepository.existsByImagen(imagen);
    }
}


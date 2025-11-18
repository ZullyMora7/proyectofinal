package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Gato;
import usco.edu.co.CatSoup.repository.GatoRepository;
import usco.edu.co.CatSoup.service.GatoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatoServiceImpl implements GatoService {

    private final GatoRepository gatoRepository;

    @Override
    public List<Gato> findAll() {
        return gatoRepository.findAll();
    }

    @Override
    public Gato findById(Long id) {
        return gatoRepository.findById(id).orElse(null);
    }

    @Override
    public Gato save(Gato gato) {
        return gatoRepository.save(gato);
    }

    @Override
    public void delete(Long id) {
        gatoRepository.deleteById(id);
    }

    // ✔ Validación de nombre único
    @Override
    public boolean existeNombre(String nombre) {
        return gatoRepository.existsByNombre(nombre);
    }

    // ✔ Validación de imagen única
    @Override
    public boolean existeImagen(String imagen) {
        return gatoRepository.existsByImagen(imagen);
    }
}

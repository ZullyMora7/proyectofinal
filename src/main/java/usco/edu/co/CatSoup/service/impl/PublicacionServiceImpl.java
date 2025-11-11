package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Publicacion;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.repository.PublicacionRepository;
import usco.edu.co.CatSoup.service.PublicacionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;

    @Override
    public List<Publicacion> findAll() {
        return publicacionRepository.findAll();
    }

    @Override
    public List<Publicacion> findByUser(User user) {
        return publicacionRepository.findAll()
                .stream()
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .toList();
    }

    @Override
    public Publicacion save(Publicacion p) {
        return publicacionRepository.save(p);
    }

    @Override
    public void delete(Long id) {
        publicacionRepository.deleteById(id);
    }

    @Override
    public Publicacion findById(Long id) {
        return publicacionRepository.findById(id).orElse(null);
    }
}


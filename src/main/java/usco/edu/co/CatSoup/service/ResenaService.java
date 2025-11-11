package usco.edu.co.CatSoup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Resena;
import usco.edu.co.CatSoup.repository.ResenaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository repo;

    public List<Resena> getResenas(String tipo, Long idObjeto) {
        return repo.findByObjetoIdAndTipoObjeto(idObjeto, tipo);
    }

    public void save(Resena resena) {
        repo.save(resena);
    }
    
    public Resena findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}

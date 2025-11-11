package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Vestuario;
import usco.edu.co.CatSoup.repository.VestuarioRepository;
import usco.edu.co.CatSoup.service.VestuarioService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VestuarioServiceImpl implements VestuarioService {

    private final VestuarioRepository vestuarioRepository;

    @Override
    public List<Vestuario> findAll() {
        return vestuarioRepository.findAll();
    }

    @Override
    public Vestuario findById(Long id) {
        return vestuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Vestuario save(Vestuario vestuario) {
        return vestuarioRepository.save(vestuario);
    }

    @Override
    public void delete(Long id) {
        vestuarioRepository.deleteById(id);
    }
}

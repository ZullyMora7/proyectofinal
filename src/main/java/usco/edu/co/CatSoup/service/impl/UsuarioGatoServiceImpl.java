package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Gato;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.UsuarioGato;
import usco.edu.co.CatSoup.repository.UsuarioGatoRepository;
import usco.edu.co.CatSoup.service.UsuarioGatoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioGatoServiceImpl implements UsuarioGatoService {

    private final UsuarioGatoRepository usuarioGatoRepository;

    @Override
    public List<UsuarioGato> findByUser(User user) {
        return usuarioGatoRepository.findByUser(user);
    }

    @Override
    public UsuarioGato marcarObtenido(User user, Gato gato) {

        UsuarioGato ug = usuarioGatoRepository
                .findByUser(user)
                .stream()
                .filter(e -> e.getGato().getId().equals(gato.getId()))
                .findFirst()
                .orElseGet(() -> {
                    UsuarioGato nuevo = new UsuarioGato();
                    nuevo.setUser(user);
                    nuevo.setGato(gato);
                    return nuevo;
                });

        ug.setObtenido(true);
        return usuarioGatoRepository.save(ug);
    }

    @Override
    public UsuarioGato desmarcarObtenido(User user, Gato gato) {
        UsuarioGato ug = usuarioGatoRepository
                .findByUser(user)
                .stream()
                .filter(e -> e.getGato().getId().equals(gato.getId()))
                .findFirst()
                .orElse(null);

        if (ug != null) {
            usuarioGatoRepository.delete(ug); // ✅ elimina el registro completamente
            return ug;
        }

        return null;
    }

}

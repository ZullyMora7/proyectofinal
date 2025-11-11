package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.Vestuario;
import usco.edu.co.CatSoup.model.UsuarioVestuario;
import usco.edu.co.CatSoup.repository.UsuarioVestuarioRepository;
import usco.edu.co.CatSoup.service.UsuarioVestuarioService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioVestuarioServiceImpl implements UsuarioVestuarioService {

    private final UsuarioVestuarioRepository usuarioVestuarioRepository;

    @Override
    public List<UsuarioVestuario> findByUser(User user) {
        return usuarioVestuarioRepository.findByUser(user);
    }

    @Override
    public UsuarioVestuario marcarObtenido(User user, Vestuario vestuario) {

        UsuarioVestuario uv = usuarioVestuarioRepository
                .findByUser(user)
                .stream()
                .filter(e -> e.getVestuario().getId().equals(vestuario.getId()))
                .findFirst()
                .orElseGet(() -> {
                    UsuarioVestuario nuevo = new UsuarioVestuario();
                    nuevo.setUser(user);
                    nuevo.setVestuario(vestuario);
                    return nuevo;
                });

        uv.setObtenido(true);
        return usuarioVestuarioRepository.save(uv);
    }

    @Override
    public UsuarioVestuario desmarcarObtenido(User user, Vestuario vestuario) {

        UsuarioVestuario uv = usuarioVestuarioRepository
                .findByUser(user)
                .stream()
                .filter(e -> e.getVestuario().getId().equals(vestuario.getId()))
                .findFirst()
                .orElse(null);

        if (uv != null) {
            uv.setObtenido(false);
            return usuarioVestuarioRepository.save(uv);
        }

        return null;
    }
}

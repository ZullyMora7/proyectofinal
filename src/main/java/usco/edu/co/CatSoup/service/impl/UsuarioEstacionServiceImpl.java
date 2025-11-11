package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Estacion;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.model.UsuarioEstacion;
import usco.edu.co.CatSoup.repository.UsuarioEstacionRepository;
import usco.edu.co.CatSoup.service.UsuarioEstacionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioEstacionServiceImpl implements UsuarioEstacionService {

    private final UsuarioEstacionRepository usuarioEstacionRepository;

    @Override
    public List<UsuarioEstacion> findByUser(User user) {
        return usuarioEstacionRepository.findByUser(user);
    }

    @Override
    public UsuarioEstacion marcarObtenido(User user, Estacion estacion) {

        UsuarioEstacion ue = usuarioEstacionRepository
                .findByUser(user)
                .stream()
                .filter(e -> e.getEstacion().getId().equals(estacion.getId()))
                .findFirst()
                .orElseGet(() -> {
                    UsuarioEstacion nuevo = new UsuarioEstacion();
                    nuevo.setUser(user);
                    nuevo.setEstacion(estacion);
                    return nuevo;
                });

        ue.setObtenido(true);
        return usuarioEstacionRepository.save(ue);
    }

    @Override
    public UsuarioEstacion desmarcarObtenido(User user, Estacion estacion) {

        UsuarioEstacion ue = usuarioEstacionRepository
                .findByUser(user)
                .stream()
                .filter(e -> e.getEstacion().getId().equals(estacion.getId()))
                .findFirst()
                .orElse(null);

        if (ue != null) {
            ue.setObtenido(false);
            return usuarioEstacionRepository.save(ue);
        }

        return null;
    }
}

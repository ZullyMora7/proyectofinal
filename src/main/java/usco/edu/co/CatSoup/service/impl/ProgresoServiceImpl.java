package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.repository.*;
import usco.edu.co.CatSoup.service.ProgresoService;

@Service
@RequiredArgsConstructor
public class ProgresoServiceImpl implements ProgresoService {

    private final GatoRepository gatoRepository;
    private final VestuarioRepository vestuarioRepository;
    private final EstacionRepository estacionRepository;

    private final UsuarioGatoRepository usuarioGatoRepository;
    private final UsuarioVestuarioRepository usuarioVestuarioRepository;
    private final UsuarioEstacionRepository usuarioEstacionRepository;

    // ✅ GATOS
    @Override
    public int totalGatos() {
        return (int) gatoRepository.count();
    }

    @Override
    public int gatosObtenidos(User user) {
        return usuarioGatoRepository.findByUser(user)
                .stream()
                .filter(ug -> ug.isObtenido())
                .toList()
                .size();
    }

    @Override
    public double porcentajeGatos(User user) {
        if (totalGatos() == 0) return 0;
        return (gatosObtenidos(user) * 100.0) / totalGatos();
    }

    // ✅ VESTUARIOS
    @Override
    public int totalVestuarios() {
        return (int) vestuarioRepository.count();
    }

    @Override
    public int vestuariosObtenidos(User user) {
        return usuarioVestuarioRepository.findByUser(user)
                .stream()
                .filter(uv -> uv.isObtenido())
                .toList()
                .size();
    }

    @Override
    public double porcentajeVestuarios(User user) {
        if (totalVestuarios() == 0) return 0;
        return (vestuariosObtenidos(user) * 100.0) / totalVestuarios();
    }

    // ✅ ESTACIONES
    @Override
    public int totalEstaciones() {
        return (int) estacionRepository.count();
    }

    @Override
    public int estacionesObtenidas(User user) {
        return usuarioEstacionRepository.findByUser(user)
                .stream()
                .filter(ue -> ue.isObtenido())
                .toList()
                .size();
    }

    @Override
    public double porcentajeEstaciones(User user) {
        if (totalEstaciones() == 0) return 0;
        return (estacionesObtenidas(user) * 100.0) / totalEstaciones();
    }
}


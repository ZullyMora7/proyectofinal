package usco.edu.co.CatSoup.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import usco.edu.co.CatSoup.model.Role;
import usco.edu.co.CatSoup.model.User;
import usco.edu.co.CatSoup.repository.RoleRepository;
import usco.edu.co.CatSoup.repository.UserRepository;
import usco.edu.co.CatSoup.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ============================================================
    // REGISTRO DE USUARIO NORMAL
    // ============================================================
    @Override
    public User registerUser(User user) {

        // Validación de email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        // Validación de username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar rol por defecto
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        user.setRole(userRole);

        return userRepository.save(user);
    }

    // ============================================================
    // BUSQUEDAS
    // ============================================================
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ============================================================
    // GUARDAR - USADO POR ADMIN AL CREAR
    // ============================================================
    @Override
    public void save(User user) {

        if (user.getId() == null) {
            // Crear usuario normal
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    // ============================================================
    // ELIMINAR
    // ============================================================
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // ============================================================
    // ACTUALIZAR USUARIO (PARA ADMIN)
    // ============================================================
    @Override
    public void updateUser(User user) {
        Optional<User> existingUserOpt = userRepository.findById(user.getId());

        if (existingUserOpt.isEmpty()) {
            throw new RuntimeException("USER_NOT_FOUND");
        }

        User existingUser = existingUserOpt.get();

        // VALIDAR EMAIL SI FUE MODIFICADO
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("EMAIL_EXISTS");
            }
        }

        // VALIDAR USERNAME SI FUE MODIFICADO
        if (!existingUser.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                throw new IllegalArgumentException("USERNAME_EXISTS");
            }
        }

        // Mantener contraseña actual si no se cambió
        user.setPassword(existingUser.getPassword());

        // Guardar cambios
        userRepository.save(user);
    }
}




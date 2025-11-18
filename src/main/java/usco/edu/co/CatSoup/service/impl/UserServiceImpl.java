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
    // REGISTRO NORMAL (USUARIO NO ADMIN)
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

        // SOLO SI NO TIENE ROL → asigna ROLE_USER
        if (user.getRole() == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);
            }
            user.setRole(userRole);
        }

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
    // GUARDAR (USADO POR ADMIN)
    // ============================================================
    @Override
    public void save(User user) {

        // Validaciones
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }

        // Encriptar contraseña si viene normal
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
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
    // ACTUALIZAR USUARIO
    // ============================================================
    @Override
    public void updateUser(User user) {

        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        // Validar email si cambia
        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        // Validar username si cambia
        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }

        // Actualizar username y email
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        // Actualizar rol
        existingUser.setRole(user.getRole());

        // Si administrador cambia la contraseña
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }
}





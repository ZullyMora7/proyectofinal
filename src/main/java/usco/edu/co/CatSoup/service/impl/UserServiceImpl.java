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
    private final BCryptPasswordEncoder passwordEncoder;

    // ============================================================
    // REGISTRO NORMAL (USUARIO NO ADMIN)
    // ============================================================
    @Override
    public User registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
    // GUARDAR (ADMIN)
    // ============================================================
    @Override
    public void save(User user) {

        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent() && !existingUsername.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }

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

        if (!existingUser.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("EMAIL_EXISTS");
        }

        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("USERNAME_EXISTS");
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }
}

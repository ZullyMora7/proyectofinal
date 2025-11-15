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

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateUser(User user) {
        Optional<User> existingUserOpt = userRepository.findById(user.getId());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (!existingUser.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                    throw new IllegalArgumentException("EMAIL_EXISTS");
                }
            }

            if (!existingUser.getUsername().equals(user.getUsername())) {
                if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                    throw new IllegalArgumentException("USERNAME_EXISTS");
                }
            }

            user.setPassword(existingUser.getPassword());

            userRepository.save(user);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}



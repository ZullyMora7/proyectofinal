package usco.edu.co.CatSoup.service;

import usco.edu.co.CatSoup.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void save(User user);
    
    void deleteById(Long id);

    void updateUser(User user);

    // 🔧 NUEVO: Permite buscar un usuario por ID
    Optional<User> findById(Long id);
}

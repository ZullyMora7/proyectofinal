package usco.edu.co.CatSoup.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import usco.edu.co.CatSoup.model.*;
import usco.edu.co.CatSoup.repository.*;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final GatoRepository gatoRepository;
    private final VestuarioRepository vestuarioRepository;
    private final EstacionRepository estacionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {

        // ✅ Crear roles si no existen
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);
        }

        if (roleRepository.findByName("ROLE_USER") == null) {
            Role user = new Role();
            user.setName("ROLE_USER");
            roleRepository.save(user);
        }

        System.out.println("✅ Roles verificados/creados");

        // ✅ Obtener roles existentes
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        Role roleUser = roleRepository.findByName("ROLE_USER");

        // ⚠️ Importante: limpiar roles antiguos si venías del ManyToMany
        // EXECUTA EN SQL SI AÚN TIENES user_roles:
        // DROP TABLE user_roles;


        // ✅ Crear Usuario Admin
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

            User admin = new User();
            admin.setUsername("Administrador");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setRole(roleAdmin); // ✔️ SOLO UN ROL

            userRepository.save(admin);

            System.out.println("✅ Admin creado (admin@gmail.com / 1234)");
        }

        // ✅ Crear Usuario Normal
        if (userRepository.findByEmail("user@gmail.com").isEmpty()) {

            User user = new User();
            user.setUsername("Usuario Normal");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setRole(roleUser); // ✔️ SOLO UN ROL

            userRepository.save(user);

            System.out.println("✅ Usuario creado (user@gmail.com / 1234)");
        }

        // ✅ Crear 1 Gato
        if (gatoRepository.findAll().isEmpty()) {

            Gato g = new Gato();
            g.setNombre("Gato Chef");
            g.setDescripcion("Un gato adorable que cocina sopas deliciosas.");
            g.setImagen("https://example.com/gato-chef.jpg");

            gatoRepository.save(g);

            System.out.println("✅ Gato inicial creado");
        }

        // ✅ Crear 1 Vestuario
        if (vestuarioRepository.findAll().isEmpty()) {

            Vestuario v = new Vestuario();
            v.setNombre("Sombrero de Cocinero");
            v.setDescripcion("Cabeza");
            v.setImagen("https://example.com/sombrero.jpg");

            vestuarioRepository.save(v);

            System.out.println("✅ Vestuario inicial creado");
        }

        // ✅ Crear 1 Estación
        if (estacionRepository.findAll().isEmpty()) {

            Estacion e = new Estacion();
            e.setNombre("Cocina Principal");
            e.setDescripcion("La estación inicial del juego.");
            e.setImagen("https://example.com/cocina.jpg");

            estacionRepository.save(e);

            System.out.println("✅ Estación inicial creada");
        }
    }
}







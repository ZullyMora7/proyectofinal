package usco.edu.co.CatSoup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usco.edu.co.CatSoup.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}


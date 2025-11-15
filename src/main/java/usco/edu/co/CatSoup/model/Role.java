package usco.edu.co.CatSoup.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import java.util.HashSet;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
}

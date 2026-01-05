package gr.aueb.cf.gpoapp.model.static_data;

import gr.aueb.cf.gpoapp.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Set<User> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void addUser(User user) {
        if (users == null) users = new HashSet<>();
        users.add(user);
        user.setRegion(this);
    }

}
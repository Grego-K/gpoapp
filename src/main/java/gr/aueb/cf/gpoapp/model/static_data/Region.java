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
@ToString(exclude = "users") // Προσθήκη για debugging
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Διασφάλιση ακεραιότητας
    private String name;

    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

}
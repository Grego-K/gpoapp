package gr.aueb.cf.gpoapp.model;

import gr.aueb.cf.gpoapp.model.static_data.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "suppliers")
public class Supplier extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(unique = true, nullable = false, length = 9)
    private String vat;

    @Column(nullable = false)
    private String companyName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 10)
    private String phoneNumber;

    // Σύνδεση με Region
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }
}
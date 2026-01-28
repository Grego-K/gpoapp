package gr.aueb.cf.gpoapp.model;

import gr.aueb.cf.gpoapp.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orders")
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Το length = 50 διασφαλίζει ότι η στήλη στη MySQL θα δημιουργηθεί με επαρκές μέγεθος
     * ώστε να μην συμβαίνει το σφάλμα "Data truncated".
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    // Μια παραγγελία - πολλά OrderItems
    // Σώζονται αυτόματα τα items μαζί με την παραγγελία
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    //Bi-directional Consistency
    public void addOrderItem(OrderItem item) {
        if (item != null) {
            orderItems.add(item);
            item.setOrder(this);
        }
    }

    public void removeOrderItem(OrderItem item) {
        if (item != null) {
            orderItems.remove(item);
            item.setOrder(null);
        }
    }

    // Υπολογίζει το συνολικό ποσό της παραγγελίας αθροίζοντας τα επιμέρους items.
    public BigDecimal getTotalAmount() {
        if (orderItems == null) return BigDecimal.ZERO;
        return orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    /**
     * Επιστρέφει τα ονόματα των προμηθευτών για αυτή την παραγγελία.
     * Απαιτεί Deep FETCH JOIN στο Repository για να αποφευχθεί το LazyInitializationException.
     */
    public List<String> getDistinctSuppliers() {
        return orderItems.stream()
                .map(item -> item.getProduct().getSupplier().getCompanyName())
                .distinct()
                .toList();
    }
}
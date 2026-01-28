package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import java.util.List;

public interface IOrderService {
    List<Order> findAllOrdersByPharmacist(User user);
    Order findOrderByUuid(String uuid);

    // Προσθήκη προϊόντος στην ενεργή παραγγελία (active - order logic)
    void addProductToOrder(User user, Long productId, Integer quantity);

    // Οριστικοποίηση και υποβολή της παραγγελίας
    void submitOrder(String uuid);

    // Ακύρωση εκκρεμούς παραγγελίας
    void cancelOrder(String uuid);
}
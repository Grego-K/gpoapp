package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.filters.OrderFilters;
import gr.aueb.cf.gpoapp.model.Order;
import gr.aueb.cf.gpoapp.model.User;
import org.springframework.data.domain.Page;

public interface IOrderService {
    // Αλλαγή για να υποστηρίζει φιλτράρισμα και pagination
    Page<Order> findAllOrdersByPharmacist(User user, OrderFilters filters);

    Order findOrderByUuid(String uuid);
    void addProductToOrder(User user, Long productId, Integer quantity);
    void submitOrder(String uuid);
    void cancelOrder(String uuid);
}
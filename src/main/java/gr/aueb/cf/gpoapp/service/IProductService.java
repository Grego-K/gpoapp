package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product saveProduct(ProductDTO productDTO) throws Exception;

    // Μετονομασία σε findAll για χρήση σε API
    List<Product> findAll();

    List<Product> findAllProducts();
    Product findProductById(Long id) throws Exception;
    Optional<Product> findProductByProductName(String productName);
    List<Product> findProductsBySupplierId(Long supplierId);
    Page<Product> getFilteredProducts(ProductFilters filters);
}
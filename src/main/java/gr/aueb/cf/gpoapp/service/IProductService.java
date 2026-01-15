package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.model.Product;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product saveProduct(ProductDTO productDTO) throws Exception;

    List<Product> findAllProducts();
    Product findProductById(Long id) throws Exception;
    Optional<Product> findProductByProductName(String productName);
    List<Product> findProductsBySupplierId(Long supplierId);
}
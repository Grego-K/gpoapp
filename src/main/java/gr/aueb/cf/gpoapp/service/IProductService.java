package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    Page<ProductDTO> getFilteredProductsDTO(ProductFilters filters);

    Product saveProduct(ProductDTO productDTO) throws Exception;

    List<Product> findAll();

    List<Product> findAllProducts();

    Product findProductById(Long id) throws Exception;

    Optional<Product> findProductByProductName(String productName);

    List<Product> findProductsBySupplierId(Long supplierId);

    Page<Product> getFilteredProducts(ProductFilters filters);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id) throws Exception;

    ProductDTO findProductDTOById(Long id) throws Exception;
}

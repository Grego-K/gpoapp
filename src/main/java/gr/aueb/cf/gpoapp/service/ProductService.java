package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.model.Category;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.model.Supplier;
import gr.aueb.cf.gpoapp.repository.CategoryRepository;
import gr.aueb.cf.gpoapp.repository.ProductRepository;
import gr.aueb.cf.gpoapp.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public Product saveProduct(ProductDTO productDTO) throws Exception {
        Product product = new Product();

        // Mapping πεδίων
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setBasePrice(productDTO.getBasePrice());
        product.setGpoPrice(productDTO.getGpoPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        // Ανάκτηση Category και Supplier
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new Exception("Η κατηγορία με ID " + productDTO.getCategoryId() + " δεν βρέθηκε"));

        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                .orElseThrow(() -> new Exception("Ο προμηθευτής με ID " + productDTO.getSupplierId() + " δεν βρέθηκε"));

        product.setCategory(category);
        product.setSupplier(supplier);

        return productRepository.save(product);
    }

    // Επιστρέφει σελιδοποιημένα και φιλτραρισμένα προϊόντα.
    @Override
    @Transactional(readOnly = true)
    public Page<Product> getFilteredProducts(ProductFilters filters) {
        return productRepository.findFiltered(
                filters.getProductName(),
                filters.getCategoryId(),
                filters.getPageable()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAllWithRelations();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("Το προϊόν δεν βρέθηκε"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductByProductName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName, null)
                .stream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsBySupplierId(Long supplierId) {
        // Επιστρέφει λίστα προϊόντων του προμηθευτή
        return productRepository.findBySupplierId(supplierId);
    }
}
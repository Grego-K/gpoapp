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

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        // Βριίσκουμε το υπάρχον προϊόν
        Product product = productRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new Exception("Το προϊόν προς ενημέρωση δεν βρέθηκε"));

        // Ενημερώνουμε τα βασικά πεδία
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setBasePrice(productDTO.getBasePrice());
        product.setGpoPrice(productDTO.getGpoPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        // Ενημερώνουμε Category και Supplier αν έχουν αλλάξει
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new Exception("Η νέα κατηγορία δεν βρέθηκε"));
        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                .orElseThrow(() -> new Exception("Ο νέος προμηθευτής δεν βρέθηκε"));

        product.setCategory(category);
        product.setSupplier(supplier);

        // Αποθήκευση των αλλαγών
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) throws Exception {
        // Ελέγχουμε αν υπάρχει πριν τη διαγραφή
        if (!productRepository.existsById(id)) {
            throw new Exception("Το προϊόν με ID " + id + " δεν βρέθηκε για να διαγραφεί");
        }
        productRepository.deleteById(id);
    }

    // Υλοποίηση της findAll για το REST API
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Επιστρέφει σελιδοποιημένα και φιλτραρισμένα προϊόντα
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
        // Χρήση της μεθόδου με Fetch Join για να αποφύγουμε LazyInit issues στο REST API
        return productRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new Exception("Το προϊόν με ID " + id + " δεν βρέθηκε"));
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
        return productRepository.findBySupplierId(supplierId);
    }
}
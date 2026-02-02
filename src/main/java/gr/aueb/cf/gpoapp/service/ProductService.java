package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.core.filters.ProductFilters;
import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.dto.RebateTierDTO;
import gr.aueb.cf.gpoapp.model.*;
import gr.aueb.cf.gpoapp.model.enums.PeriodType;
import gr.aueb.cf.gpoapp.repository.CategoryRepository;
import gr.aueb.cf.gpoapp.repository.ProductRepository;
import gr.aueb.cf.gpoapp.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gr.aueb.cf.gpoapp.mapper.ProductMapper;
import gr.aueb.cf.gpoapp.repository.ProductProgressRepository;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductProgressRepository progressRepository;
    private final ProductMapper productMapper;

    /**
     * Επιστρέφει όλα τα προϊόντα converted σε DTOs,
     * εμπλουτισμένα με το volume του τρέχοντος τριμήνου.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllProductsForCatalog() {
        String currentPeriod = getCurrentPeriodLabel();

        return productRepository.findAllWithRelations().stream()
                .map(product -> {
                    // Αναζήτηση προόδου για το συγκεκριμένο προϊόν και τρίμηνο
                    ProductProgress progress = progressRepository
                            .findByProductIdAndPeriodLabel(product.getId(), currentPeriod)
                            .orElse(null); // Ο Mapper θα χειριστεί το null ως 0 volume!

                    return productMapper.mapToProductDTO(product, progress);
                })
                .collect(Collectors.toList());
    }

    /**
     * Helper μέθοδος που παράγει το label βάσει ημερομηνίας.
     * Π.χ. Φεβρουάριος 2026 -> "2026_Q1"
     */
    private String getCurrentPeriodLabel() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int quarter = (month - 1) / 3 + 1;
        return year + "_Q" + quarter;
    }

    @Override
    @Transactional
    public Product saveProduct(ProductDTO productDTO) throws Exception {
        // Fail-fast checks: Αν κάτι είναι null, σταματάμε αμέσως την εκτέλεση
        Objects.requireNonNull(productDTO.getProductName(), "Product Name is required");
        Objects.requireNonNull(productDTO.getBasePrice(), "Base Price is required");
        Objects.requireNonNull(productDTO.getGpoPrice(), "GPO Price is required");
        Objects.requireNonNull(productDTO.getStockQuantity(), "Stock Quantity is required");
        Objects.requireNonNull(productDTO.getCategoryId(), "Category ID is required");
        Objects.requireNonNull(productDTO.getSupplierId(), "Supplier ID is required");

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

        // Προσθήκη Rebate Tiers αν υπάρχουν στο DTO
        if (productDTO.getRebateTiers() != null) {
            for (RebateTierDTO tierDTO : productDTO.getRebateTiers()) {
                // Έλεγχος για κενά δεδομένα από τη φόρμα
                if (tierDTO.getMinQuantity() != null && tierDTO.getRebateAmount() != null) {
                    RebateTier tier = new RebateTier();
                    tier.setMinQuantity(tierDTO.getMinQuantity());
                    tier.setMaxQuantity(tierDTO.getMaxQuantity());
                    tier.setRebateAmount(tierDTO.getRebateAmount());
                    tier.setPeriodType(PeriodType.QUARTER); // Default
                    tier.setProduct(product);
                    product.getRebateTiers().add(tier);
                }
            }
        }

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

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getFilteredProductsDTO(ProductFilters filters) {
        String currentPeriod = getCurrentPeriodLabel();

        // 1. Φέρνουμε τη σελίδα με τα Entities
        Page<Product> productPage = productRepository.findFiltered(
                filters.getProductName(),
                filters.getCategoryId(),
                filters.getPageable()
        );

        // 2. Μετατρέπουμε κάθε Product σε ProductDTO χρησιμοποιώντας τον Mapper
        return productPage.map(product -> {
            ProductProgress progress = progressRepository
                    .findByProductIdAndPeriodLabel(product.getId(), currentPeriod)
                    .orElse(null);

            return productMapper.mapToProductDTO(product, progress);
        });
    }
}
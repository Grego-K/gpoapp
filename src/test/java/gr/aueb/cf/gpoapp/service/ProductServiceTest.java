package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.dto.ProductDTO;
import gr.aueb.cf.gpoapp.mapper.ProductMapper;
import gr.aueb.cf.gpoapp.model.Category;
import gr.aueb.cf.gpoapp.model.Product;
import gr.aueb.cf.gpoapp.model.Supplier;
import gr.aueb.cf.gpoapp.repository.CategoryRepository;
import gr.aueb.cf.gpoapp.repository.ProductProgressRepository;
import gr.aueb.cf.gpoapp.repository.ProductRepository;
import gr.aueb.cf.gpoapp.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit Tests για το ProductService.
 * Χρησιμοποιούμε το Mockito για τη δημιουργία mock αντικειμένων των dependencies (repositories).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProductProgressRepository progressRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;
    private Supplier testSupplier;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        // Δημιουργία test data
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Αντιβιοτικά");

        testSupplier = new Supplier();
        testSupplier.setId(1L);
        testSupplier.setCompanyName("Test Supplier");
        testSupplier.setVat("123456789");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setProductName("Paracetamol");
        testProduct.setDescription("Παυσίπονο");
        testProduct.setBasePrice(new BigDecimal("10.00"));
        testProduct.setGpoPrice(new BigDecimal("8.00"));
        testProduct.setStockQuantity(100);
        testProduct.setCategory(testCategory);
        testProduct.setSupplier(testSupplier);

        testProductDTO = new ProductDTO();
        testProductDTO.setProductName("Paracetamol");
        testProductDTO.setDescription("Παυσίπονο");
        testProductDTO.setBasePrice(new BigDecimal("10.00"));
        testProductDTO.setGpoPrice(new BigDecimal("8.00"));
        testProductDTO.setStockQuantity(100);
        testProductDTO.setCategoryId(1L);
        testProductDTO.setSupplierId(1L);
    }

    @Test
    @DisplayName("Θα πρέπει να επιστρέφει όλα τα προϊόντα")
    void testFindAllProducts() {
        // Arrange (Προετοιμασία)
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findAllWithRelations()).thenReturn(expectedProducts);

        // Act (Εκτέλεση)
        List<Product> actualProducts = productService.findAllProducts();

        // Assert (Έλεγχος)
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals("Paracetamol", actualProducts.get(0).getProductName());
        verify(productRepository, times(1)).findAllWithRelations();
    }

    @Test
    @DisplayName("Θα πρέπει να βρίσκει προϊόν με βάση το ID")
    void testFindProductById_Success() throws Exception {
        // Arrange
        when(productRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product foundProduct = productService.findProductById(1L);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        assertEquals("Paracetamol", foundProduct.getProductName());
        verify(productRepository, times(1)).findByIdWithRelations(1L);
    }

    @Test
    @DisplayName("Θα πρέπει να πετάει exception όταν το προϊόν δεν βρεθεί")
    void testFindProductById_NotFound() {
        // Arrange
        when(productRepository.findByIdWithRelations(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productService.findProductById(999L);
        });

        assertTrue(exception.getMessage().contains("δεν βρέθηκε"));
        verify(productRepository, times(1)).findByIdWithRelations(999L);
    }

    @Test
    @DisplayName("Θα πρέπει να αποθηκεύει νέο προϊόν επιτυχώς")
    void testSaveProduct_Success() throws Exception {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product savedProduct = productService.saveProduct(testProductDTO);

        // Assert
        assertNotNull(savedProduct);
        assertEquals("Paracetamol", savedProduct.getProductName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(supplierRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Θα πρέπει να πετάει exception όταν η κατηγορία δεν βρεθεί")
    void testSaveProduct_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productService.saveProduct(testProductDTO);
        });

        assertTrue(exception.getMessage().contains("κατηγορία"));
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Θα πρέπει να πετάει NullPointerException όταν το όνομα προϊόντος είναι null")
    void testSaveProduct_NullProductName() {
        // Arrange
        testProductDTO.setProductName(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            productService.saveProduct(testProductDTO);
        });

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Θα πρέπει να διαγράφει προϊόν επιτυχώς")
    void testDeleteProduct_Success() throws Exception {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Θα πρέπει να πετάει exception όταν προσπαθούμε να διαγράψουμε ανύπαρκτο προϊόν")
    void testDeleteProduct_NotFound() {
        // Arrange
        when(productRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            productService.deleteProduct(999L);
        });

        assertTrue(exception.getMessage().contains("δεν βρέθηκε"));
        verify(productRepository, times(1)).existsById(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Θα πρέπει να ενημερώνει προϊόν επιτυχώς")
    void testUpdateProduct_Success() throws Exception {
        // Arrange
        ProductDTO updateDTO = new ProductDTO();
        updateDTO.setProductName("Updated Paracetamol");
        updateDTO.setDescription("Updated Description");
        updateDTO.setBasePrice(new BigDecimal("12.00"));
        updateDTO.setGpoPrice(new BigDecimal("9.00"));
        updateDTO.setStockQuantity(150);
        updateDTO.setCategoryId(1L);
        updateDTO.setSupplierId(1L);

        when(productRepository.findByIdWithRelations(1L)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product updatedProduct = productService.updateProduct(1L, updateDTO);

        // Assert
        assertNotNull(updatedProduct);
        verify(productRepository, times(1)).findByIdWithRelations(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Θα πρέπει να βρίσκει προϊόντα με βάση το Supplier ID")
    void testFindProductsBySupplierId() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findBySupplierId(1L)).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.findProductsBySupplierId(1L);

        // Assert
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        verify(productRepository, times(1)).findBySupplierId(1L);
    }
}

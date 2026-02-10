package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.dto.CategoryDTO;
import gr.aueb.cf.gpoapp.model.Category;
import gr.aueb.cf.gpoapp.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAllCategories_success() {
        // Arrange
        Category category = new Category();
        category.setName("Test Category");
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // Act
        List<Category> categories = categoryService.findAllCategories();

        // Assert
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
        assertEquals("Test Category", categories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void saveCategory_success() throws Exception {
        // Arrange
        CategoryDTO dto = new CategoryDTO();
        dto.setName("New Category");
        dto.setDescription("Description");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("New Category");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        Category result = categoryService.saveCategory(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Category", result.getName());
    }

    @Test
    void findCategoryById_failure() {
        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            categoryService.findCategoryById(99L);
        });

        assertEquals("Η κατηγορία δεν βρέθηκε", exception.getMessage());
    }
}

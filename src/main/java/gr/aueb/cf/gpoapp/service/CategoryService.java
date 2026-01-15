package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.dto.CategoryDTO;
import gr.aueb.cf.gpoapp.model.Category;
import gr.aueb.cf.gpoapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category saveCategory(CategoryDTO categoryDTO) throws Exception {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Η κατηγορία δεν βρέθηκε"));
    }
}
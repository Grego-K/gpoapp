package gr.aueb.cf.gpoapp.service;

import gr.aueb.cf.gpoapp.dto.CategoryDTO;
import gr.aueb.cf.gpoapp.model.Category;
import java.util.List;

public interface ICategoryService {
    Category saveCategory(CategoryDTO categoryDTO) throws Exception;
    List<Category> findAllCategories();
    Category findCategoryById(Long id) throws Exception;
}
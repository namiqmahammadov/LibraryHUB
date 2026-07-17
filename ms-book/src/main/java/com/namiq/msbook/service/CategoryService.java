package com.namiq.msbook.service;

import com.namiq.msbook.dao.entity.Category;
import com.namiq.msbook.dao.repository.CategoryRepository;
import com.namiq.msbook.dto.request.CategoryRequest;
import com.namiq.msbook.dto.response.CategoryResponse;
import com.namiq.msbook.dto.response.UserResponse;
import com.namiq.msbook.enums.Role;
import com.namiq.msbook.exception.CategoryNameAlreadyExistsException;
import com.namiq.msbook.exception.CategoryNotFoundException;
import com.namiq.msbook.exception.UnauthorizedOperationException;
import com.namiq.msbook.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserClient userClient;

    private void checkAdmin(String token) {
        UserResponse user = userClient.getCUrrentUser(token);

        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedOperationException("Only admins can perform this operation.");
        }
    }

    public void createCategory(String token, CategoryRequest categoryRequest) {
        checkAdmin(token);
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new CategoryNameAlreadyExistsException("Category name already exists");
        }
        Category category = categoryMapper.toEntity(categoryRequest);
        categoryRepository.save(category);
    }


    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Integer id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return categoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategory(String token,Integer id, CategoryRequest categoryRequest) {
       checkAdmin(token);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryMapper.updateCategory(category, categoryRequest);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    public void deleteCategoryById(String token ,Integer id) {
        checkAdmin(token);
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException(("Category not found"));
        }

    }

}

package com.namiq.msbook.controller;

import com.namiq.msbook.dto.request.CategoryRequest;
import com.namiq.msbook.dto.response.CategoryResponse;
import com.namiq.msbook.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;

    @PostMapping
    public void createCategory(
            @RequestHeader("Authorization") String token,
            @RequestBody CategoryRequest request) {

        categoryService.createCategory(token, request);
    }
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id,
            @RequestBody CategoryRequest request) {

        return categoryService.updateCategory(token, id, request);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {

        categoryService.deleteCategoryById(token, id);
    }
}

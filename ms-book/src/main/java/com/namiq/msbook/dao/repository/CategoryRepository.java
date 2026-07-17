package com.namiq.msbook.dao.repository;

import com.namiq.msbook.dao.entity.Category;
import com.namiq.msbook.enums.CategoryName;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName( CategoryName name);
}

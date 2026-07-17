package com.namiq.msbook.mapper;

import com.namiq.msbook.dao.entity.Category;
import com.namiq.msbook.dto.request.CategoryRequest;
import com.namiq.msbook.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface CategoryMapper {

 @Mapping(target = "id", ignore = true)
 @Mapping(target = "createdAt", ignore = true)
 Category toEntity(CategoryRequest request);

 CategoryResponse toResponse(Category category);

 @Mapping(target = "id", ignore = true)
 @Mapping(target = "createdAt", ignore = true)
 void updateCategory(@MappingTarget Category category, CategoryRequest request);
}

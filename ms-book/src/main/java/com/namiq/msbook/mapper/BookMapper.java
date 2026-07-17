package com.namiq.msbook.mapper;

import com.namiq.msbook.dao.entity.Book;
import com.namiq.msbook.dao.entity.Category;
import com.namiq.msbook.dto.request.BookRequest;
import com.namiq.msbook.dto.response.BookResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "availableCopies", expression = "java(request.getTotalCopies())")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Book toEntity(BookRequest request, Category category);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "availableCopies", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            @MappingTarget Book book,
            BookRequest request,
            Category category
    );


    @Mapping(target = "categoryId", source = "category.id")
    BookResponse toResponse(Book book);

}
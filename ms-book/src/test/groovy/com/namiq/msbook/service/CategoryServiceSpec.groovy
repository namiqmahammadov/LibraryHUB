package com.namiq.msbook.service

import com.namiq.msbook.dao.entity.Category
import com.namiq.msbook.dao.repository.CategoryRepository
import com.namiq.msbook.dto.request.CategoryRequest
import com.namiq.msbook.enums.CategoryName
import com.namiq.msbook.exception.CategoryNameAlreadyExistsException
import com.namiq.msbook.mapper.CategoryMapper
import spock.lang.Specification

class CategoryServiceSpec extends Specification {
    CategoryRepository categoryRepository = Mock()
    CategoryMapper categoryMapper = Mock()
    CategoryService categoryService = new CategoryService(categoryRepository, categoryMapper)

    def "should throw exception when category name already exists"() {
        given:
        def request = new CategoryRequest()
        request.setName(CategoryName.History)

        and:
        1 * categoryRepository.existsByName(CategoryName.History) >> true

        when:
        categoryService.createCategory(request)

        then:
        def exception = thrown(CategoryNameAlreadyExistsException)
        exception.message == "Category name already exists"

        0 * categoryMapper._
        0 * categoryRepository.save(_)
    }

    def "should create category successfully"() {
        given:
        def request = new CategoryRequest()
        request.setName(CategoryName.History)
        def category = new Category()
        category.setName(CategoryName.History)
        and:
        1 * categoryRepository.existsByName(CategoryName.History) >> false
        1 * categoryMapper.toEntity(request) >> category
        1 * categoryRepository.save(category)
        when:
        categoryService.createCategory(request)
        then:
        noExceptionThrown()

    }
}
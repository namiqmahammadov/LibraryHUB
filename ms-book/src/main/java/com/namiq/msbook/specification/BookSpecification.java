package com.namiq.msbook.specification;

import com.namiq.msbook.dao.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> filter(Integer categoryId, String author) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(
                        cb.equal(root.get("category").get("id"), categoryId)
                );
            }

            if (author != null && !author.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("author")),
                                "%" + author.toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
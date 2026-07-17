package com.namiq.msbook.dao.repository;

import com.namiq.msbook.dao.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer>,
        JpaSpecificationExecutor<Book> {

    @Query("""
SELECT b
FROM  Book b
JOIN FETCH b.category
""")
    List<Book>findAllWithCategory();

    @Query("""
            SELECT b
            FROM Book b
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Book> searchBooks(@Param("keyword") String keyword);
    Boolean existsByIsbn(String isbn);

}

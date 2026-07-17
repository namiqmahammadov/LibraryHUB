package com.namiq.msuser.dao.repository;

import com.namiq.msuser.dao.entity.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowHistoryRepository extends JpaRepository<BorrowHistory, Integer> {
    Optional<BorrowHistory> findTopByUserIdAndBookIdOrderByBorrowAtDesc(
            Integer userId,
            Integer bookId
    );

}

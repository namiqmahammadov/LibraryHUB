package com.namiq.msbook.dao.repository;

import com.namiq.msbook.dao.entity.Borrow;
import com.namiq.msbook.enums.Status;
import org.mapstruct.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow,Integer> {

    List<Borrow> findByUserId(Integer userId);

    Boolean existsByUserIdAndBookIdAndStatus(Integer userId, Integer bookId, Status status);

    List<Borrow> findByStatusAndDueDateBefore(Status status, LocalDate date);
}

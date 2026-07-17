package com.namiq.msuser.dao.entity;

import com.namiq.msuser.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "borrow_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BorrowHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private String bookTitle;
    private LocalDateTime borrowAt;
    private LocalDateTime returnAt;
    @Enumerated(EnumType.STRING)
    private Status status;

}

package com.namiq.msbook.dao.entity;

import com.namiq.msbook.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrows")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private LocalDateTime    borrowAt;
    private LocalDate dueDate;
    private LocalDateTime returnAt;
    @Enumerated(EnumType.STRING)
    private Status status;
}

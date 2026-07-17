package com.namiq.msbook.dto.response;

import com.namiq.msbook.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowResponse {

    private Integer id;

    private Integer bookId;

    private Integer userId;

    private LocalDateTime borrowAt;

    private LocalDate dueDate;

    private LocalDateTime returnAt;

    private Status status;
}
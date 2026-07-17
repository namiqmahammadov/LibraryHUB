package com.namiq.msbook.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class BorrowRequest {

        @NotNull(message = "Book id is required")
        private Integer bookId;
    }


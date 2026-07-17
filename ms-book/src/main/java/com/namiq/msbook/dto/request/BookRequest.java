package com.namiq.msbook.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Author cannot be empty")
    private String author;

    @NotBlank(message = "ISBN cannot be empty")
    private String isbn;

    private String description;

    @NotNull(message = "Category id is required")
    private Integer categoryId;

    @NotNull(message = "Total copies is required")
    @Positive(message = "Total copies must be greater than 0")
    private Integer totalCopies;

    @NotNull(message = "Published year is required")
    @Min(value = 1000)
    @Max(value = 9999)
    private Integer publishedYear;
}
package com.namiq.msbook.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {
    @NotBlank(message = "Title cannot be blank.")
    @Size(max = 255, message = "Title cannot exceed 255 characters.")
    private String title;

    @NotBlank(message = "Author cannot be blank.")
    @Size(max = 100, message = "Author cannot exceed 100 characters.")
    private String author;

    @NotBlank(message = "ISBN cannot be blank.")
    private String isbn;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters.")
    private String description;

    @NotNull(message = "Category ID is required.")
    private Integer categoryId;

    @NotNull(message = "Total copies is required.")
    @Positive(message = "Total copies must be greater than 0.")
    private Integer totalCopies;

    @NotNull(message = "Published year is required.")
    @Min(value = 1000, message = "Published year must be a valid 4-digit year.")
    @Max(value = 9999, message = "Published year must be a valid 4-digit year.")
    private Integer publishedYear;
}
package com.namiq.msbook.dto.request;

import com.namiq.msbook.enums.CategoryName;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotNull(message = "Category name cannot be null.")
    private CategoryName name;

    @NotBlank(message = "Description cannot be blank.")
    @Size(max = 100, message = "Description cannot exceed 100 characters.")
    private String description;
}

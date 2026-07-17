package com.namiq.msbook.dao.entity;

import com.namiq.msbook.enums.CategoryName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private CategoryName name;
    private String description;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

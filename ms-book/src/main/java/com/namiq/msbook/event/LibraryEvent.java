package com.namiq.msbook.event;

import com.namiq.msbook.enums.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryEvent {

    private Integer userId;

    private Integer bookId;

    private String bookTitle;

    private EventType eventType;

    private LocalDateTime eventTime;
}
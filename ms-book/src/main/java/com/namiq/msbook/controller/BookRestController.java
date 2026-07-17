package com.namiq.msbook.controller;

import com.namiq.msbook.dto.request.BookRequest;
import com.namiq.msbook.dto.response.BookResponse;
import com.namiq.msbook.dto.response.PageResponse;
import com.namiq.msbook.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestHeader("Authorization") String token,
                                                   @Valid @RequestBody BookRequest bookRequest) {
        bookService.createBook(token, bookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public PageResponse<BookResponse> getAllBooks(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String author,
            @PageableDefault(
                    size = 10,
                    sort = "title",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        Page<BookResponse> page = bookService.getAllBooks(categoryId, author, pageable);

        return PageResponse.<BookResponse>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.search(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> editBooks(@RequestHeader("Authorization") String token,
                                                  @PathVariable Integer id,
                                                  @Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.editBooks(token, id, bookRequest));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@RequestHeader("Authorization") String token,
                                           @PathVariable Integer id) {
        bookService.deleteById(token, id);
        return ResponseEntity.noContent().build();
    }


}

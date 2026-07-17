package com.namiq.msbook.controller;

import com.namiq.msbook.dto.request.BorrowRequest;
import com.namiq.msbook.dto.response.BookResponse;
import com.namiq.msbook.dto.response.BorrowResponse;
import com.namiq.msbook.dto.response.PageResponse;
import com.namiq.msbook.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowRestController {
    private final BorrowService borrowService;

    @PostMapping
    public ResponseEntity<BorrowResponse> borrowBook(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody BorrowRequest borrowRequest
    ) {

        return ResponseEntity.ok(borrowService.borrowBook(token, borrowRequest));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BorrowResponse> returnBook(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(borrowService.retrunBook(id, token));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BorrowResponse>> getMyBorrow(
            @RequestHeader("Authorization") String token
    ){
        return ResponseEntity.ok(borrowService.getMyBorrow(token));
    }
    @GetMapping
    public PageResponse<BorrowResponse>getAllBorrows(@RequestHeader("Authorization") String token
    , Pageable pageable){
        Page<BorrowResponse> page=borrowService.getAllBorrows(token,pageable);
        return PageResponse.<BorrowResponse>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

    }
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowResponse>>getOverDueBorrows(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(borrowService.getOverDueBorrows(token));
    }

}

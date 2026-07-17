package com.namiq.msbook.mapper;

import com.namiq.msbook.dao.entity.Borrow;
import com.namiq.msbook.dto.request.BorrowRequest;
import com.namiq.msbook.dto.response.BorrowResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BorrowMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "borrowAt", ignore = true)
    @Mapping(target = "dueDate", ignore = true)
    @Mapping(target = "returnAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Borrow toEntity(BorrowRequest request);


    BorrowResponse toResponse(Borrow borrow);

}
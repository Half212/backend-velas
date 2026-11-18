package com.velassj.backend.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderItemRequestDTO(
        @NotNull Long productId,
        String productName,
        String categoryName,
        @NotNull @Min(1) Integer quantity,
        @NotNull @Min(0) BigDecimal unitPrice
) {}

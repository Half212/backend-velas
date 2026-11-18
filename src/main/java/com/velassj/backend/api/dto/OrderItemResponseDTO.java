package com.velassj.backend.api.dto;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long id,
        Long productId,
        String productName,
        String categoryName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {}

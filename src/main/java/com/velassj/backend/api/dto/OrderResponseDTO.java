package com.velassj.backend.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String customerName,
        String customerPhone,
        String deliveryNotes,
        String storeLocation,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt,
        String userEmail,
        List<OrderItemResponseDTO> items
) {}

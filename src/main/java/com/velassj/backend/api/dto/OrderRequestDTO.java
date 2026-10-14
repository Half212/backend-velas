package com.velassj.backend.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record OrderRequestDTO(
        @NotBlank String customerName,
        String customerPhone,
        String deliveryNotes,
        String storeLocation,
        @NotNull BigDecimal totalAmount,
        String userEmail,
        @NotEmpty @Valid List<OrderItemRequestDTO> items
) {}

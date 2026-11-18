package com.velassj.backend.api.controller;

import com.velassj.backend.api.dto.OrderRequestDTO;
import com.velassj.backend.api.dto.OrderResponseDTO;
import com.velassj.backend.domain.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO createOrder(@RequestBody @Valid OrderRequestDTO request) {
        return orderService.create(request);
    }

    @GetMapping
    public List<OrderResponseDTO> getOrders(@RequestParam(required = false) String period) {
        if (period != null && !period.isBlank()) {
            return orderService.findByPeriod(period);
        }
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }
}

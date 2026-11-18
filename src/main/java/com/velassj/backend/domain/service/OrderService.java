package com.velassj.backend.domain.service;

import com.velassj.backend.api.dto.OrderItemRequestDTO;
import com.velassj.backend.api.dto.OrderItemResponseDTO;
import com.velassj.backend.api.dto.OrderRequestDTO;
import com.velassj.backend.api.dto.OrderResponseDTO;
import com.velassj.backend.domain.entity.Order;
import com.velassj.backend.domain.entity.OrderItem;
import com.velassj.backend.domain.entity.Product;
import com.velassj.backend.domain.repositories.OrderRepository;
import com.velassj.backend.domain.repositories.ProductRepository;
import com.velassj.backend.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO dto) {
        Order order = new Order();
        order.setCustomerName(dto.customerName().trim());
        order.setCustomerPhone(dto.customerPhone() != null ? dto.customerPhone().trim() : null);
        order.setDeliveryNotes(dto.deliveryNotes() != null ? dto.deliveryNotes().trim() : null);
        order.setStoreLocation(dto.storeLocation() != null && !dto.storeLocation().isBlank() ? dto.storeLocation().toLowerCase() : "matriz");
        order.setStatus("COMPLETED");
        order.setCreatedAt(LocalDateTime.now());

        if (dto.userEmail() != null && !dto.userEmail().isBlank()) {
            userRepository.findByEmail(dto.userEmail().trim()).ifPresent(order::setUser);
        }

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDto : dto.items()) {
            Product product = productRepository.findById(itemDto.productId()).orElse(null);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            
            String productName = itemDto.productName();
            if (productName == null || productName.isBlank()) {
                productName = product != null ? product.getName() : "Produto #" + itemDto.productId();
            }
            item.setProductName(productName);

            String categoryName = itemDto.categoryName();
            if ((categoryName == null || categoryName.isBlank()) && product != null && product.getCategory() != null) {
                categoryName = product.getCategory().getName();
            }
            item.setCategoryName(categoryName != null ? categoryName : "Geral");

            item.setQuantity(itemDto.quantity());
            item.setUnitPrice(itemDto.unitPrice());

            BigDecimal itemTotal = itemDto.unitPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            item.setTotalPrice(itemTotal);
            calculatedTotal = calculatedTotal.add(itemTotal);

            if (product != null && product.getStockQuantity() != null) {
                int newStock = Math.max(0, product.getStockQuantity() - itemDto.quantity());
                product.setStockQuantity(newStock);
                productRepository.save(product);
            }

            order.addItem(item);
        }

        if (dto.totalAmount() != null && dto.totalAmount().compareTo(BigDecimal.ZERO) > 0) {
            order.setTotalAmount(dto.totalAmount());
        } else {
            order.setTotalAmount(calculatedTotal);
        }

        Order saved = orderRepository.save(order);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> findByPeriod(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        return orderRepository.findByCreatedAtAfterOrderByCreatedAtDesc(startDate).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado com ID: " + id));
        return convertToDTO(order);
    }

    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Pedido nao encontrado com ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    private LocalDateTime calculateStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        if (period == null) return now.minusDays(30);

        return switch (period.toLowerCase()) {
            case "today" -> LocalDate.now().atStartOfDay();
            case "7days" -> now.minusDays(7);
            case "30days" -> now.minusDays(30);
            case "month" -> LocalDate.now().withDayOfMonth(1).atStartOfDay();
            case "year" -> LocalDate.now().withDayOfYear(1).atStartOfDay();
            default -> LocalDateTime.of(2000, 1, 1, 0, 0);
        };
    }

    private OrderResponseDTO convertToDTO(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getProduct() != null ? item.getProduct().getId() : null,
                        item.getProductName(),
                        item.getCategoryName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                ))
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getDeliveryNotes(),
                order.getStoreLocation(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUser() != null ? order.getUser().getEmail() : null,
                itemDTOs
        );
    }
}

package com.velassj.backend.api.controller;
import com.velassj.backend.api.ProductRequestDTO;
import com.velassj.backend.api.ProductResponseDTO;
import com.velassj.backend.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(@RequestBody @Valid ProductRequestDTO request) {
        return productService.create(request);
    }
}

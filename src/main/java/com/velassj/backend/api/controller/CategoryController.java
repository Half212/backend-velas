package com.velassj.backend.api.controller;

import com.velassj.backend.api.dto.CategoryRequestDTO;
import com.velassj.backend.api.dto.CategoryResponseDTO;
import com.velassj.backend.domain.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO createCategory(@RequestBody @Valid CategoryRequestDTO request) {
        return categoryService.create(request);
    }
}

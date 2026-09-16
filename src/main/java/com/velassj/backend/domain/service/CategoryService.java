package com.velassj.backend.domain.service;

import com.velassj.backend.api.dto.CategoryRequestDTO;
import com.velassj.backend.api.dto.CategoryResponseDTO;
import com.velassj.backend.domain.entity.Category;
import com.velassj.backend.domain.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryResponseDTO(cat.getId(), cat.getName()))
                .toList();
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.name());
        category = categoryRepository.save(category);
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}

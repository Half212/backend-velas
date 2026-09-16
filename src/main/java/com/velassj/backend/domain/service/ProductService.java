package com.velassj.backend.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.velassj.backend.domain.repositories.CategoryRepository;
import com.velassj.backend.domain.repositories.ProductRepository;
import com.velassj.backend.domain.entity.Category;
import com.velassj.backend.domain.entity.Product;
import com.velassj.backend.api.ProductRequestDTO;
import com.velassj.backend.api.ProductResponseDTO;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setCategory(category);

        if (dto.image() != null && !dto.image().isBlank()) {
            String base64Data = dto.image();
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                product.setImage(imageBytes);
            } catch (IllegalArgumentException e) {
                // Se Base64 inválido, ignora ou aceita nulo
            }
        }

        product = productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setCategory(category);

        if (dto.image() != null) {
            if (dto.image().isBlank()) {
                product.setImage(null);
            } else {
                String base64Data = dto.image();
                if (base64Data.contains(",")) {
                    base64Data = base64Data.split(",")[1];
                }
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                    product.setImage(imageBytes);
                } catch (IllegalArgumentException e) {
                    // Ignora
                }
            }
        }

        product = productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDTO convertToDTO(Product product) {
        String imageBase64 = null;
        if (product.getImage() != null && product.getImage().length > 0) {
            imageBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(product.getImage());
        }

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getName(),
                imageBase64);
    }
}
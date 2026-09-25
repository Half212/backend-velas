package com.velassj.backend.api.dto;

public record CarouselSlideDTO(
    String id,
    String image,
    String title,
    String description,
    String buttonText,
    String buttonLink,
    Boolean active,
    Integer order
) {}
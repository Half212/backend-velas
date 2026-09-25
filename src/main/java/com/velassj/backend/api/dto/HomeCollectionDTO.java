package com.velassj.backend.api.dto;

public record HomeCollectionDTO(
    String id,
    String tag,
    String title,
    String subtitle,
    String image,
    String buttonText,
    String link,
    Boolean active,
    Integer order
) {}
package com.velassj.backend.domain.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carousel_slide")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CarouselSlide {

    @Id
    @EqualsAndHashCode.Include
    @Column(length = 64)
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "button_text", length = 100)
    private String buttonText;

    @Column(name = "button_link", length = 255)
    private String buttonLink;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 1;
}
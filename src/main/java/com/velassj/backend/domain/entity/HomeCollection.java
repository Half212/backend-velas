package com.velassj.backend.domain.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "home_collection")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HomeCollection {

    @Id
    @EqualsAndHashCode.Include
    @Column(length = 64)
    private String id;

    @Column(length = 100)
    private String tag;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(name = "button_text", length = 100)
    private String buttonText;

    @Column(length = 255)
    private String link;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 1;
}
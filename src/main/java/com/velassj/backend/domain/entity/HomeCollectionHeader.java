package com.velassj.backend.domain.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "home_collection_header")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HomeCollectionHeader {

    @Id
    @EqualsAndHashCode.Include
    private Long id = 1L;

    @Column(length = 100)
    private String tag;

    @Column(nullable = false, length = 255)
    private String title;
}
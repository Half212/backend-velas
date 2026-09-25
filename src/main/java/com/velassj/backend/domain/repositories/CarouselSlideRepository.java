package com.velassj.backend.domain.repositories;

import com.velassj.backend.domain.entity.CarouselSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarouselSlideRepository extends JpaRepository<CarouselSlide, String> {
    List<CarouselSlide> findAllByOrderByOrderIndexAsc();
}
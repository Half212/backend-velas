package com.velassj.backend.api.controller;

import com.velassj.backend.api.dto.CarouselSlideDTO;
import com.velassj.backend.domain.service.CarouselSlideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carousel")
@RequiredArgsConstructor
public class CarouselController {

    private final CarouselSlideService carouselSlideService;

    @GetMapping
    public List<CarouselSlideDTO> getSlides() {
        return carouselSlideService.findAll();
    }

    @PutMapping
    public List<CarouselSlideDTO> saveSlides(@RequestBody List<CarouselSlideDTO> slides) {
        return carouselSlideService.saveAll(slides);
    }
}
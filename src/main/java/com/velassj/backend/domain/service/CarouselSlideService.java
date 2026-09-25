package com.velassj.backend.domain.service;

import com.velassj.backend.api.dto.CarouselSlideDTO;
import com.velassj.backend.domain.entity.CarouselSlide;
import com.velassj.backend.domain.repositories.CarouselSlideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarouselSlideService {

    private final CarouselSlideRepository repository;

    @Transactional(readOnly = true)
    public List<CarouselSlideDTO> findAll() {
        return repository.findAllByOrderByOrderIndexAsc().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public List<CarouselSlideDTO> saveAll(List<CarouselSlideDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return findAll();
        }

        List<String> incomingIds = dtoList.stream()
                .map(CarouselSlideDTO::id)
                .filter(id -> id != null && !id.isBlank())
                .toList();

        List<CarouselSlide> existing = repository.findAll();
        for (CarouselSlide slide : existing) {
            if (!incomingIds.contains(slide.getId())) {
                repository.delete(slide);
            }
        }

        List<CarouselSlide> toSave = new ArrayList<>();
        int index = 1;
        for (CarouselSlideDTO dto : dtoList) {
            CarouselSlide entity = new CarouselSlide();
            entity.setId((dto.id() != null && !dto.id().isBlank()) ? dto.id() : "slide-" + System.currentTimeMillis() + "-" + index);
            entity.setImage(dto.image());
            entity.setTitle(dto.title() != null ? dto.title() : "");
            entity.setDescription(dto.description());
            entity.setButtonText(dto.buttonText());
            entity.setButtonLink(dto.buttonLink());
            entity.setActive(dto.active() != null ? dto.active() : true);
            entity.setOrderIndex(dto.order() != null ? dto.order() : index);
            toSave.add(entity);
            index++;
        }

        repository.saveAll(toSave);
        return findAll();
    }

    private CarouselSlideDTO toDTO(CarouselSlide entity) {
        return new CarouselSlideDTO(
                entity.getId(),
                entity.getImage(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getButtonText(),
                entity.getButtonLink(),
                entity.getActive(),
                entity.getOrderIndex()
        );
    }
}
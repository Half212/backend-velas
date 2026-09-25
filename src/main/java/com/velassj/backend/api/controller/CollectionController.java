package com.velassj.backend.api.controller;

import com.velassj.backend.api.dto.CollectionsPayloadDTO;
import com.velassj.backend.domain.service.HomeCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final HomeCollectionService homeCollectionService;

    @GetMapping
    public CollectionsPayloadDTO getCollections() {
        return homeCollectionService.getPayload();
    }

    @PutMapping
    public CollectionsPayloadDTO saveCollections(@RequestBody CollectionsPayloadDTO payload) {
        return homeCollectionService.savePayload(payload);
    }
}
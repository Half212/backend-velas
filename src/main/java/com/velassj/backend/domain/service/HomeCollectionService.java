package com.velassj.backend.domain.service;

import com.velassj.backend.api.dto.CollectionsPayloadDTO;
import com.velassj.backend.api.dto.HomeCollectionDTO;
import com.velassj.backend.api.dto.HomeCollectionHeaderDTO;
import com.velassj.backend.domain.entity.HomeCollection;
import com.velassj.backend.domain.entity.HomeCollectionHeader;
import com.velassj.backend.domain.repositories.HomeCollectionHeaderRepository;
import com.velassj.backend.domain.repositories.HomeCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCollectionService {

    private final HomeCollectionRepository collectionRepository;
    private final HomeCollectionHeaderRepository headerRepository;

    @Transactional(readOnly = true)
    public CollectionsPayloadDTO getPayload() {
        List<HomeCollectionDTO> collections = collectionRepository.findAllByOrderByOrderIndexAsc().stream()
                .map(this::toDTO)
                .toList();

        HomeCollectionHeader header = headerRepository.findById(1L)
                .orElse(new HomeCollectionHeader());

        HomeCollectionHeaderDTO headerDTO = new HomeCollectionHeaderDTO(
                header.getTag() != null ? header.getTag() : "Artesanato & Fé",
                header.getTitle() != null ? header.getTitle() : "Nossas Coleções"
        );

        return new CollectionsPayloadDTO(collections, headerDTO);
    }

    @Transactional
    public CollectionsPayloadDTO savePayload(CollectionsPayloadDTO payload) {
        if (payload == null) {
            return getPayload();
        }

        if (payload.header() != null) {
            HomeCollectionHeader header = headerRepository.findById(1L)
                    .orElse(new HomeCollectionHeader());
            header.setId(1L);
            header.setTag(payload.header().tag());
            header.setTitle(payload.header().title());
            headerRepository.save(header);
        }

        if (payload.collections() != null && !payload.collections().isEmpty()) {
            List<String> incomingIds = payload.collections().stream()
                    .map(HomeCollectionDTO::id)
                    .filter(id -> id != null && !id.isBlank())
                    .toList();

            List<HomeCollection> existing = collectionRepository.findAll();
            for (HomeCollection col : existing) {
                if (!incomingIds.contains(col.getId())) {
                    collectionRepository.delete(col);
                }
            }

            List<HomeCollection> toSave = new ArrayList<>();
            int index = 1;
            for (HomeCollectionDTO dto : payload.collections()) {
                HomeCollection entity = new HomeCollection();
                entity.setId((dto.id() != null && !dto.id().isBlank()) ? dto.id() : "collection-" + System.currentTimeMillis() + "-" + index);
                entity.setTag(dto.tag());
                entity.setTitle(dto.title() != null ? dto.title() : "");
                entity.setSubtitle(dto.subtitle());
                entity.setImage(dto.image());
                entity.setButtonText(dto.buttonText());
                entity.setLink(dto.link());
                entity.setActive(dto.active() != null ? dto.active() : true);
                entity.setOrderIndex(dto.order() != null ? dto.order() : index);
                toSave.add(entity);
                index++;
            }

            collectionRepository.saveAll(toSave);
        }

        return getPayload();
    }

    private HomeCollectionDTO toDTO(HomeCollection entity) {
        return new HomeCollectionDTO(
                entity.getId(),
                entity.getTag(),
                entity.getTitle(),
                entity.getSubtitle(),
                entity.getImage(),
                entity.getButtonText(),
                entity.getLink(),
                entity.getActive(),
                entity.getOrderIndex()
        );
    }
}
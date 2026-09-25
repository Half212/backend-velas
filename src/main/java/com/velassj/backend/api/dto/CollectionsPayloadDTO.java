package com.velassj.backend.api.dto;

import java.util.List;

public record CollectionsPayloadDTO(
    List<HomeCollectionDTO> collections,
    HomeCollectionHeaderDTO header
) {}
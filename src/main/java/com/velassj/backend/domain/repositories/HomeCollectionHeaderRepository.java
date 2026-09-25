package com.velassj.backend.domain.repositories;

import com.velassj.backend.domain.entity.HomeCollectionHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeCollectionHeaderRepository extends JpaRepository<HomeCollectionHeader, Long> {
}
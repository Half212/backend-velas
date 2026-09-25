package com.velassj.backend.domain.repositories;

import com.velassj.backend.domain.entity.HomeCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeCollectionRepository extends JpaRepository<HomeCollection, String> {
    List<HomeCollection> findAllByOrderByOrderIndexAsc();
}
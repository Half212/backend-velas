package com.velassj.backend.domain.repositories;

import com.velassj.backend.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);

    List<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.createdAt >= :startDate ORDER BY o.createdAt DESC")
    List<Order> findAllWithItemsSince(@Param("startDate") LocalDateTime startDate);
}

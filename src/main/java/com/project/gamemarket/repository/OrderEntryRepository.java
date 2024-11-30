package com.project.gamemarket.repository;

import com.project.gamemarket.domain.order.OrderEntry;
import com.project.gamemarket.repository.entity.OrderEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderEntryRepository extends JpaRepository<OrderEntryEntity, Long> {
}

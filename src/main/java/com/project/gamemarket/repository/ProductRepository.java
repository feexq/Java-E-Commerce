package com.project.gamemarket.repository;

import com.project.gamemarket.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByTitleIgnoreCase(String title);
    Optional<ProductEntity> findByTitleIgnoreCase(String title);
}

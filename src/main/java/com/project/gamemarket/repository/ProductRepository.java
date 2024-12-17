package com.project.gamemarket.repository;

import com.project.gamemarket.repository.entity.ProductEntity;
import com.project.gamemarket.repository.projection.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByTitleIgnoreCase(String title);
    Optional<ProductEntity> findByTitleIgnoreCase(String title);

    @Query("SELECT p.id AS id, p.title AS title, p.shortDescription AS shortDescription, " +
            "p.price AS price " +
            "FROM ProductEntity p " +
            "ORDER BY p.price DESC")
    List<ProductSummary> findAllCustomByDesc();

}

package com.project.gamemarket.repository;

import com.project.gamemarket.repository.entity.OrderEntity;
import com.project.gamemarket.repository.projection.OrderSummary;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends NaturalIdRepository<OrderEntity, String> {

    void deleteById(Long id);

    @Query("SELECT o.id AS id, o.cart_id AS cart_id, o.total_price AS total_price, " +
            "o.payment_reference AS payment_reference, c.customerReference AS customerReference " +
            "FROM OrderEntity o JOIN o.customer c " +
            "ORDER BY o.total_price DESC")
    List<OrderSummary> findAllCustom();

}

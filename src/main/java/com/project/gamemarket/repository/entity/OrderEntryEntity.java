package com.project.gamemarket.repository.entity;

import com.project.gamemarket.domain.ProductDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_entry")
public class OrderEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_entry_id_seq")
    @SequenceGenerator(name = "order_entry_id_seq", sequenceName = "order_entry_id_seq")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity productEntity;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @Column(name = "price", nullable = false)
    Double price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "order_id",referencedColumnName = "id", nullable = false)
    OrderEntity order_id;

}

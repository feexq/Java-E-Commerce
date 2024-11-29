package com.project.gamemarket.repository.entity;

import com.project.gamemarket.common.CategoryType;
import com.project.gamemarket.common.DeviceType;
import com.project.gamemarket.domain.order.OrderEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq")
    Long id;

    @Column(nullable = false, unique = true)
    String title;

    @Column(name = "short_description")
    String shortDescription;

    Double price;

    String developer;

    @ElementCollection(targetClass = DeviceType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_device_types",
            joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.ORDINAL)
    List<DeviceType> device_type;

    @ElementCollection(targetClass = CategoryType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "product_category_genres",
            joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.ORDINAL)
    List<CategoryType> category_genre;

    /**
     * To remove the product.
     * I don't know if it is advisable to delete the product when it is already placed in the order (I think it's pointless)
     * CascadeType.PERSIST
     * **/
    @OneToMany(mappedBy = "productEntity", cascade = {CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderEntryEntity> orderEntries;

}

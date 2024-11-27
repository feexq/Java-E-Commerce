package com.project.gamemarket.repository.entity;

import com.project.gamemarket.common.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq")
    Long id;

    String name;
    @Column(unique = true, nullable = false)
    String phoneNumber;
    @Column(unique = true, nullable = false)
    String email;
    String region;

    @NaturalId
    @Column(nullable = false, unique = true)
    UUID customerReference;

    @ElementCollection(targetClass = DeviceType.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "customer_device_type",
            joinColumns = @JoinColumn(name = "customer_id"))
    @Enumerated(EnumType.ORDINAL)
    List<DeviceType> deviceTypeList;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    List<OrderEntity> orders;
}

package com.project.gamemarket.repository;

import com.project.gamemarket.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends NaturalIdRepository<CustomerEntity, UUID> {

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    void deleteById(Long id);

}

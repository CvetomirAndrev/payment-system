package com.emerchantpay.repository;

import com.emerchantpay.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository<T extends User>  extends JpaRepository<T, Long> {

    Optional<User> findByName(String name);

    Optional<T> findById(Long id);

}

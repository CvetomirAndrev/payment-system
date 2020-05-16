package com.emerchantpay.repository;

import com.emerchantpay.model.entities.Transaction;
import com.emerchantpay.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

}

package com.emerchantpay.model.entities;

import com.emerchantpay.model.enums.TransactionStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Min(value = 1, message = "Amount must be greater than zero")
    private Long amount;

    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    //  TODO : add validation for email
    @Column(name = "customer_email")
    private String customerEmail;

    //  TODO : add validation for phone
    @Column(name = "customer_phone")
    private String customerPhone;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Merchant referenceId;

    // TODO don't like this to reference to parent Transaction
    @Column(name = "belong_to")
    private String belongTo;


}

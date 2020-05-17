package com.emerchantpay.model.entities;

import com.emerchantpay.model.enums.TransactionStatus;
import com.emerchantpay.model.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Min(value = 0, message = "Amount must be greater or equal zero")
    private Long amount;

    //  TODO : add validation for status
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    //  TODO : add validation for email
    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    //  TODO : add validation for type
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @NotNull
    private Merchant referenceId;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "initial_transaction_id")
    @JsonIgnore
    private Transaction initialTransaction;

}

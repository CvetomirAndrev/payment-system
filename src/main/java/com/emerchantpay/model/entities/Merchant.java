package com.emerchantpay.model.entities;


import com.emerchantpay.model.enums.MerchantStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("M")
@Data
public class Merchant extends User {

    @Column(name = "merchant_status")
    private MerchantStatus merchantStatus;

    // TODO : add validation to only sym Transactions that are Charge and approve
    @Column(name = "total_transaction_sum")
//    @Formula("select sum(t.amount) from Transactions t where t.reference_id = :id")
    private Long totalTransactionSum;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "referenceId")
    private List<Transaction> transactions;
}

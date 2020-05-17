package com.emerchantpay.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("M")
@Getter
@Setter
public class Merchant extends User {

    @Column(name = "status")
    @NotNull
    private boolean active;

    // TODO : add validation to check only Approve AUTHORIZE_TRANSACTION
    @Transient
    @Formula("(select sum(t.amount) from paymentsystem.transaction as t where t.referenceid_id = id and t.type = 'CHARGE' And t.status = 'APPROVED')")
    private Long totalTransactionSum;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "referenceId")
    @JsonIgnore
    private List<Transaction> transactions;
}

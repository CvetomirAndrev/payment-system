package com.emerchantpay.model.dto.request;

import com.emerchantpay.model.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionRequestDto {

    private Long amount;

    private TransactionType transactionType;

    private String customerEmail;

    private String customerPhone;

    private Long merchantId;

    private String belongTo;
}

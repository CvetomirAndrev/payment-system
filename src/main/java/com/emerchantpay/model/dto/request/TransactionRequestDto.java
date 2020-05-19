package com.emerchantpay.model.dto.request;

import com.emerchantpay.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequestDto {

    private Long amount;

    private TransactionType transactionType;

    private String customerEmail;

    private String customerPhone;

    private Long merchantId;

    private String belongTo;
}

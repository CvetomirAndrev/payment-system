package com.emerchantpay.service;

import com.emerchantpay.model.dto.request.TransactionRequestDto;
import com.emerchantpay.model.entities.Transaction;

public interface TransactionState {

    Transaction createTransaction(TransactionRequestDto transactionDto, Transaction relatedTransaction);
}

package com.emerchantpay.model.enums;


import com.emerchantpay.exeptions.TransactionCreateException;
import com.emerchantpay.model.dto.request.TransactionRequestDto;
import com.emerchantpay.model.entities.Transaction;
import com.emerchantpay.service.TransactionState;

import static com.emerchantpay.model.enums.TransactionStatus.*;

public enum TransactionType implements TransactionState {

    AUTHORIZE_TRANSACTION {
        @Override
        public Transaction createTransaction(TransactionRequestDto transactionDto, Transaction relatedTransaction) {
            if (relatedTransaction != null) {
                throw new TransactionCreateException("Authorize Transaction cant relate to other transaction");
            }
            return buildTransaction(transactionDto, null, APPROVED);
        }
    },
    // TODO: Organize the validation. The validation for transactionType and TransactionStatus can be extracted
    CHARGE {
        @Override
        public Transaction createTransaction(TransactionRequestDto transactionDto, Transaction relatedTransaction) {
            validateTransactionType(relatedTransaction, "Charge", AUTHORIZE_TRANSACTION);
            if (validateTransactionStatus(relatedTransaction)) {
                return buildTransaction(transactionDto, relatedTransaction, ERROR);
            }
            if (transactionDto.getAmount() > relatedTransaction.getAmount()) {
                throw new TransactionCreateException("Charge Transaction amount need to lower that Authorize transaction amount with id "
                        + relatedTransaction.getId() + " and customer email " + relatedTransaction.getCustomerEmail());
            }
            relatedTransaction.setAmount(relatedTransaction.getAmount() - transactionDto.getAmount());

            return buildTransaction(transactionDto, relatedTransaction, APPROVED);
        }
    },

    REFUND {
        @Override
        public Transaction createTransaction(TransactionRequestDto transactionDto, Transaction relatedTransaction) {
            validateTransactionType(relatedTransaction, "Refund", CHARGE);
            if (validateTransactionStatus(relatedTransaction)) {
                return buildTransaction(transactionDto, relatedTransaction, ERROR);
            }
            Long amount = relatedTransaction.getAmount();
            Transaction initialTransaction = relatedTransaction.getInitialTransaction();
            initialTransaction.setAmount(initialTransaction.getAmount() + amount);
            relatedTransaction.setStatus(REFUNDED);
            return buildTransaction(transactionDto, relatedTransaction, APPROVED);
        }
    },

    REVERSAL {
        @Override
        public Transaction createTransaction(TransactionRequestDto transactionDto, Transaction relatedTransaction) {
            validateTransactionType(relatedTransaction, "Reversal", AUTHORIZE_TRANSACTION);
            if (validateTransactionStatus(relatedTransaction)) {
                return buildTransaction(transactionDto, relatedTransaction, ERROR);
            }
            relatedTransaction.setStatus(REVERSED);
            return buildTransaction(transactionDto, relatedTransaction, APPROVED);
        }
    };

    private static boolean validateTransactionStatus(Transaction relatedTransaction) {
        return relatedTransaction.getStatus() == ERROR || relatedTransaction.getStatus() == REVERSED;
    }

    private static Transaction buildTransaction(TransactionRequestDto transactionDto, Transaction relatedTransaction, TransactionStatus status) {
        return Transaction.builder()
                .amount(transactionDto.getAmount())
                .customerEmail(transactionDto.getCustomerEmail())
                .customerPhone(transactionDto.getCustomerPhone())
                .type(transactionDto.getTransactionType())
                .status(status)
                .initialTransaction(relatedTransaction)
                .build();
    }

    private static void validateTransactionType(Transaction relatedTransaction, String msg, TransactionType type) {
        if (relatedTransaction == null || relatedTransaction.getType() != type) {
            throw new TransactionCreateException(msg + " Transaction need relation to " + type + " transaction");
        }
    }

}
